package me.rojetto.comfy;

import me.rojetto.comfy.annotation.Arg;
import me.rojetto.comfy.annotation.CommandHandler;
import me.rojetto.comfy.exception.ArgumentException;
import me.rojetto.comfy.exception.CommandTreeException;
import me.rojetto.comfy.exception.HandlerException;
import me.rojetto.comfy.exception.PathException;
import me.rojetto.comfy.tree.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public abstract class CommandManager<C extends CommandContext, S extends CommandSender> {
    private final CommandRoot root;
    private final List<CommandListener> listeners;
    private final Map<String, List<AbstractMap.Entry<Method, CommandListener>>> handlerMethods;

    public CommandManager() {
        this.root = new CommandRoot();
        this.listeners = new ArrayList<>();
        this.handlerMethods = new HashMap<>();
    }

    public CommandRoot getRoot() {
        return root;
    }

    public void addCommand(Literal commandNode) {
        root.then(commandNode);
    }

    public void addListener(CommandListener listener) {
        listeners.add(listener);
    }

    public void registerCommands() {
        validateTree();
        mapHandlerMethods();
        onRegisterCommands();
    }

    public List<String> tabComplete(S sender, List<String> segments) {
        List<String> suggestions = new ArrayList<>();
        List<String> segmentsCopy = new ArrayList<>(segments);
        String lastSegment = segmentsCopy.remove(segmentsCopy.size() - 1);

        C context;

        try {
            context = parseSegments(sender, segmentsCopy);
        } catch (PathException e) {
            return suggestions;
        } catch (ArgumentException e) {
            return suggestions;
        }

        // Special case for root node completion, because paths are weird
        CommandNode lastNode = context.getPath().getLastNode() != null ? context.getPath().getLastNode() : root;
        List<CommandNode> children = lastNode.getChildren();

        for (CommandNode child : children) {
            if (child.getSuggestions(context) != null && child.getPath().checkPermission(sender)) {
                suggestions.addAll(child.getSuggestions(context));
            }
        }

        Iterator<String> iter = suggestions.iterator();
        while (iter.hasNext()) {
            String suggestion = iter.next();
            if (!suggestion.matches("(?i)" + lastSegment + ".*")) { // Starts with "lastSegment", case insensitive
                iter.remove();
            }
        }

        return suggestions;
    }

    public void help(S sender, List<String> segments) {
        try {
            CommandPath path = root.parsePath(segments, true);
            CommandNode lastNode = path.getLastNode() != null ? path.getLastNode() : root;

            Set<CommandNode> helpfulNodes = new HashSet<>();

            helpfulNodes.addAll(lastNode.getNodesWithTag("handler", true));
            helpfulNodes.addAll(lastNode.getNodesWithTag("description", true));

            if (lastNode.isCategory()) {
                helpfulNodes.remove(lastNode); // We want help for things AFTER the last node
            }

            boolean changedSet = true;
            while (changedSet) {
                changedSet = false;
                for (CommandNode node : new HashSet<>(helpfulNodes)) { // For each node (clone the set to avoid concurrent modification)
                    if (!node.hasTag("handler")) { // Check if it's just a "category"
                        Iterator<CommandNode> iter = helpfulNodes.iterator();
                        while (iter.hasNext()) { // If it is, loop through all the nodes
                            if (node.hasChild(iter.next())) { // And if they fall in this category
                                iter.remove(); // Remove them from the list
                                changedSet = true;
                            }
                        }
                    }
                }
            }

            List<CommandPath> paths = new ArrayList<>();
            for (CommandNode node : helpfulNodes) {
                paths.add(node.getLastOptional().getPath());
            }

            Iterator<CommandPath> iter = paths.iterator();
            while (iter.hasNext()) {
                if (!iter.next().checkPermission(sender)) {
                    iter.remove();
                }
            }

            Collections.sort(paths);

            if (paths.size() > 0) {
                sender.info("Showing help for '" + lastNode.getPath() + "':");
            }

            for (CommandPath helpfulPath : paths) {
                sender.pathHelp(helpfulPath);
            }
        } catch (PathException e) {
            sender.warning(e.getMessage());
        }
    }

    public void process(S sender, String commandString) {
        List<String> segments = split(commandString);

        if (segments.size() > 0 && segments.get(segments.size() - 1).equals("?")) {
            segments.remove(segments.size() - 1);
            help(sender, segments);
            return;
        }

        try {
            C context = parseSegments(sender, segments);

            if (!context.getPath().getLastNode().isExecutable()) {
                sender.warning("This is not a complete command.");
                help(sender, segments);
            } else {
                if (context.getPath().checkPermission(sender)) {
                    callHandlerMethod(context.getPath().getLastNode().getHandler(), context);
                } else {
                    sender.warning("You do not have permission to execute this command.");
                }
            }
        } catch (PathException e) {
            sender.warning(e.getMessage());
            help(sender, segments);
        } catch (ArgumentException e) {
            sender.warning("An error occurred in argument '" + e.getArgumentName() + "'");
            sender.warning(e.getMessage());
        } catch (HandlerException e) {
            e.printStackTrace();
        }
    }

    protected List<String> split(String commandString) {
        List<String> segments = new ArrayList<>();
        boolean inQuotes = false;
        boolean escape = false;
        String segment = "";
        for (int i = 0; i < commandString.length(); i++) {
            char c = commandString.charAt(i);
            if (!escape) {
                if (c == '\\') {
                    escape = true;
                } else if (c == '"') {
                    inQuotes = !inQuotes;
                } else {
                    if (c == ' ' && !inQuotes) {
                        segments.add(segment);
                        segment = "";
                    } else {
                        segment += c;
                    }
                }
            } else {
                segment += c;
                escape = false;
            }
        }
        segments.add(segment);

        return segments;
    }

    private C parseSegments(S sender, List<String> segments) throws PathException, ArgumentException {
        CommandPath path = root.parsePath(segments, false);
        Arguments args = path.parseArguments(segments);

        C context = buildContext(sender, path, args);

        return context;
    }

    private void callHandlerMethod(String handler, C context) throws HandlerException {
        if (handlerMethods.containsKey(handler)) {
            for (AbstractMap.Entry<Method, CommandListener> pair : handlerMethods.get(handler)) {
                Method method = pair.getKey();
                CommandListener listener = pair.getValue();

                int parameterCount = method.getParameterTypes().length;
                Object[] arguments = new Object[parameterCount];

                for (int i = 0; i < parameterCount; i++) {
                    Arg annotation = null;

                    for (Annotation a : method.getParameterAnnotations()[i]) {
                        if (a instanceof Arg) {
                            annotation = (Arg) a;
                        }
                    }

                    Class parameterType = method.getParameterTypes()[i];

                    if (annotation != null && context.getArguments().exists(annotation.value())) {
                        Object value = context.getArguments().get(annotation.value());

                        if (!valueTypeFitsParameterType(value.getClass(), parameterType)) {
                            throw new HandlerException("Method argument " + annotation.value() + " should be of type " + value.getClass().getName());
                        }

                        arguments[i] = value;
                        continue;
                    }

                    if (CommandContext.class.isAssignableFrom(parameterType)) {
                        arguments[i] = context;
                        continue;
                    }

                    arguments[i] = getDefaultValue(parameterType);
                }

                try {
                    method.invoke(listener, arguments);
                } catch (IllegalAccessException e) {
                    throw new HandlerException(e.getMessage());
                } catch (InvocationTargetException e) {
                    throw new HandlerException(e.getMessage());
                }
            }
        }
    }

    private Object getDefaultValue(Class aClass) {
        Map<Class, Object> defaultValues = new HashMap<>();
        defaultValues.put(byte.class, (byte) 0);
        defaultValues.put(short.class, (short) 0);
        defaultValues.put(int.class, (int) 0);
        defaultValues.put(long.class, (long) 0);
        defaultValues.put(float.class, (float) 0);
        defaultValues.put(double.class, (double) 0);
        defaultValues.put(boolean.class, (boolean) false);
        defaultValues.put(char.class, (char) 0);

        return defaultValues.get(aClass);
    }

    private boolean valueTypeFitsParameterType(Class valueType, Class parameterType) {
        boolean fits = false;

        if (parameterType.isAssignableFrom(valueType)) {
            fits = true;
        }

        Map<Class, Class> primitiveTypes = new HashMap<>();
        primitiveTypes.put(Byte.class, byte.class);
        primitiveTypes.put(Short.class, short.class);
        primitiveTypes.put(Integer.class, int.class);
        primitiveTypes.put(Long.class, long.class);
        primitiveTypes.put(Float.class, float.class);
        primitiveTypes.put(Double.class, double.class);
        primitiveTypes.put(Boolean.class, boolean.class);
        primitiveTypes.put(Character.class, char.class);

        if (primitiveTypes.get(valueType) == parameterType) {
            fits = true;
        }

        return fits;
    }

    private void validateTree() throws CommandTreeException {
        for (CommandNode leaf : root.getLeafNodes()) {
            Map<String, Boolean> usedArgumentNames = new HashMap<>();

            for (CommandNode node : leaf.getPath().getNodeList()) {
                if (node instanceof Argument) {
                    Argument arg = (Argument) node;
                    if (usedArgumentNames.containsKey(arg.getName())) {
                        throw new CommandTreeException("Argument " + arg + " already exists in this path.");
                    } else {
                        usedArgumentNames.put(arg.getName(), true);
                    }
                }
            }
        }

        for (CommandNode executable : root.getExecutableNodes(true)) {
            if (executable.isLastExecutable()) { // If it's the last executable in this path
                if (executable.getLeafNodes().size() > 1) { // and there are branches after this one
                    throw new CommandTreeException("No branches after last executable node in a path allowed.");
                }
            }
        }
    }

    private void mapHandlerMethods() throws HandlerException {
        for (CommandListener listener : listeners) {
            for (Method method : listener.getClass().getMethods()) {
                if (method.getAnnotation(CommandHandler.class) != null) {
                    String executes = method.getAnnotation(CommandHandler.class).value();
                    addHandlerMethod(executes, method, listener);
                }
            }
        }
    }

    private void addHandlerMethod(String handler, Method method, CommandListener listener) {
        if (!handlerMethods.containsKey(handler)) {
            handlerMethods.put(handler, new ArrayList<AbstractMap.Entry<Method, CommandListener>>());
        }

        handlerMethods.get(handler).add(new AbstractMap.SimpleEntry<>(method, listener));
    }

    protected abstract C buildContext(S sender, CommandPath path, Arguments arguments);

    protected abstract void onRegisterCommands();
}

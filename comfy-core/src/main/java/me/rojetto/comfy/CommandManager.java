package me.rojetto.comfy;

import me.rojetto.comfy.exception.CommandArgumentException;
import me.rojetto.comfy.exception.CommandHandlerException;
import me.rojetto.comfy.exception.CommandPathException;
import me.rojetto.comfy.exception.CommandTreeException;
import me.rojetto.comfy.tree.CommandArgument;
import me.rojetto.comfy.tree.CommandNode;
import me.rojetto.comfy.tree.CommandPath;
import me.rojetto.comfy.tree.CommandRoot;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public abstract class CommandManager {
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

    public void addCommand(CommandNode commandNode) {
        root.child(commandNode);
    }

    public void addListener(CommandListener listener) {
        listeners.add(listener);
    }

    public void registerCommands() {
        validateTree();
        mapHandlerMethods();
        onRegisterCommands();
    }

    protected List<String> tabComplete(CommandSender sender, List<String> segments) {
        List<String> suggestions = new ArrayList<>();
        List<String> segmentsCopy = new ArrayList<>(segments);
        String lastSegment = segmentsCopy.remove(segmentsCopy.size() - 1);

        CommandContext context;

        try {
            context = buildContext(sender, segmentsCopy);
        } catch (CommandPathException e) {
            sender.warning(e.getMessage());
            return suggestions;
        } catch (CommandArgumentException e) {
            sender.warning(e.getMessage());
            return suggestions;
        }

        // Special case for root node completion, because paths are weird
        CommandNode lastNode = context.getPath().getNodeList().size() > 0 ? context.getPath().getLastNode() : root;

        for (CommandNode child : lastNode.getChildren()) {
            if (child.getSuggestions(context) != null) {
                suggestions.addAll(child.getSuggestions(context));
            }
        }

        Iterator<String> iter = suggestions.iterator();
        while (iter.hasNext()) {
            String suggestion = iter.next();
            if (!suggestion.matches("(?i)" + lastSegment + ".*")) { // Starts "lastSegment", case insensitive
                iter.remove();
            }
        }

        return suggestions;
    }

    protected void process(CommandSender sender, String commandString) {
        List<String> segments = split(commandString);

        try {
            CommandContext context = buildContext(sender, segments);

            if (!context.getPath().getLastNode().isExecutable()) {
                sender.warning("This is no complete command, silly"); // TODO: Be helpful instead
            } else {
                callHandlerMethod(context.getPath().getLastNode().getHandler(), context);
            }
        } catch (CommandPathException e) {
            sender.warning(e.getMessage());
        } catch (CommandArgumentException e) {
            sender.warning(e.getMessage());
        } catch (CommandHandlerException e) {
            e.printStackTrace();
        }
    }

    protected List<String> split(String commandString) {
        List<String> segments = new ArrayList<>();
        String remaining = commandString;

        while (remaining.indexOf(" ") != -1) {
            int index = remaining.indexOf(" ");
            segments.add(remaining.substring(0, index));
            remaining = remaining.substring(index + 1);
        }

        segments.add(remaining);

        return segments;
    }

    // TODO: Clear up confusion about buildContext methods
    private CommandContext buildContext(CommandSender sender, List<String> segments) throws CommandPathException, CommandArgumentException {
        CommandPath path = root.parsePath(segments, false);
        Arguments args = path.parseArguments(segments);

        CommandContext context = buildContext(sender, path, args);

        return context;
    }

    private void callHandlerMethod(String handler, CommandContext context) throws CommandHandlerException {
        if (handlerMethods.containsKey(handler)) {
            for (AbstractMap.Entry<Method, CommandListener> pair : handlerMethods.get(handler)) {
                Method method = pair.getKey();
                CommandListener listener = pair.getValue();

                Object[] arguments = new Object[method.getParameterCount()];

                for (int i = 0; i < method.getParameterCount(); i++) {
                    Parameter param = method.getParameters()[i];
                    Arg annotation = param.getAnnotation(Arg.class);

                    if (annotation != null && context.getArguments().exists(annotation.value())) {
                        Object value = context.getArguments().get(annotation.value());

                        if (!valueTypeFitsParameterType(value.getClass(), param.getType())) {
                            throw new CommandHandlerException("Method argument " + annotation.value() + " should be of type " + value.getClass().getName());
                        }

                        arguments[i] = value;
                        continue;
                    }

                    if (CommandContext.class.isAssignableFrom(param.getType())) {
                        arguments[i] = context;
                        continue;
                    }

                    arguments[i] = null;
                }

                try {
                    method.invoke(listener, arguments);
                } catch (IllegalAccessException e) {
                    throw new CommandHandlerException(e.getMessage()); //TODO: Proper messages
                } catch (InvocationTargetException e) {
                    throw new CommandHandlerException(e.getMessage());
                }
            }
        }
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
                if (node instanceof CommandArgument) {
                    CommandArgument arg = (CommandArgument) node;
                    if (usedArgumentNames.containsKey(arg.getName())) {
                        throw new CommandTreeException("Argument " + arg + " already exists in this path.");
                    } else {
                        usedArgumentNames.put(arg.getName(), true);
                    }
                }
            }
        }

        for (CommandNode executable : root.getExecutableNodes(true)) {
            if (executable.getExecutableNodes(true).size() == 1) { // If it's the last executable in this path
                if (executable.getLeafNodes().size() > 1) { // and there are branches after this one
                    throw new CommandTreeException("No branches after last executable node in a path allowed.");
                }
            }
        }
    }

    private void mapHandlerMethods() throws CommandHandlerException {
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

    protected abstract CommandContext buildContext(CommandSender sender, CommandPath path, Arguments arguments);

    protected abstract void onRegisterCommands();
}

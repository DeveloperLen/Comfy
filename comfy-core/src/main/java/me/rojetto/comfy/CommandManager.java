package me.rojetto.comfy;

import me.rojetto.comfy.exception.CommandArgumentException;
import me.rojetto.comfy.exception.CommandHandlerException;
import me.rojetto.comfy.exception.CommandPathException;
import me.rojetto.comfy.exception.CommandTreeException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    protected void process(CommandSender sender, String commandString) {
        List<String> segments = Arrays.asList(commandString.split(" "));

        try {
            CommandPath path = root.parsePath(segments, false);
            Arguments args = path.parseArguments(segments);

            CommandContext context = buildContext(sender, path, args);

            if (!path.getLastNode().isExecutable()) {
                sender.warning("This is no complete command, silly"); // TODO: Be helpful instead
            } else {
                callHandlerMethod(path.getLastNode().getHandler(), context);
            }
        } catch (CommandPathException e) {
            sender.warning(e.getMessage());
        } catch (CommandArgumentException e) {
            sender.warning(e.getMessage());
        } catch (CommandHandlerException e) {
            e.printStackTrace();
        }
    }

    private void callHandlerMethod(String handler, CommandContext context) throws CommandHandlerException {
        if (handlerMethods.containsKey(handler)) {
            for (AbstractMap.Entry<Method, CommandListener> pair : handlerMethods.get(handler)) {
                Method method = pair.getKey();
                CommandListener listener = pair.getValue();

                try {
                    if (method.getParameterCount() == 0) {
                        method.invoke(listener);
                    } else {
                        method.invoke(listener, context); //TODO: Fix everything about this
                    }
                } catch (IllegalAccessException e) {
                    throw new CommandHandlerException(e.getMessage()); //TODO: Proper messages
                } catch (InvocationTargetException e) {
                    throw new CommandHandlerException(e.getMessage());
                }
            }
        }
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
                    throw new CommandTreeException("No branches after last executable in a path allowed.");
                }
            }
        }
    }

    private void mapHandlerMethods() throws CommandHandlerException {
        for (CommandListener listener : listeners) {
            for (Method method : listener.getClass().getMethods()) {
                if (method.getAnnotation(CommandHandler.class) != null) {
                    String executes = method.getAnnotation(CommandHandler.class).value();

                    if (method.getParameterCount() >= 1 && !CommandContext.class.isAssignableFrom(method.getParameterTypes()[0])) {
                        throw new CommandHandlerException("The first argument of a command handler method must be a CommandContext.");
                    }

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

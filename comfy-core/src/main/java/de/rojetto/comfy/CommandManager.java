package de.rojetto.comfy;

import de.rojetto.comfy.exception.CommandArgumentException;
import de.rojetto.comfy.exception.CommandHandlerException;
import de.rojetto.comfy.exception.CommandPathException;
import de.rojetto.comfy.exception.CommandTreeException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public abstract class CommandManager {
    private final CommandRoot root;
    private final List<CommandListener> listeners;

    public CommandManager() {
        this.root = new CommandRoot();

        this.listeners = new ArrayList<>();
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
                callHandlerMethod(path.getLastNode().getExecutor(), context);
            }
        } catch (CommandPathException e) {
            sender.warning(e.getMessage());
        } catch (CommandArgumentException e) {
            sender.warning(e.getMessage());
        } catch (CommandHandlerException e) {
            e.printStackTrace();
        }
    }

    private void callHandlerMethod(String executor, CommandContext context) throws CommandHandlerException {
        for (CommandListener listener : listeners) {
            for (Method method : listener.getClass().getMethods()) {
                for (CommandHandler annotation : method.getAnnotationsByType(CommandHandler.class)) {
                    if (annotation != null) {
                        if (executor.equals(annotation.value())) {
                            try {
                                method.invoke(listener, context);
                            } catch (IllegalAccessException e) {
                                throw new CommandHandlerException(e.getMessage()); // TODO: Proper messages
                            } catch (InvocationTargetException e) {
                                throw new CommandHandlerException(e.getMessage());
                            }
                        }
                    }
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

    protected abstract CommandContext buildContext(CommandSender sender, CommandPath path, Arguments arguments);

    protected abstract void onRegisterCommands();
}

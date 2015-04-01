package de.rojetto.comfy;

import de.rojetto.comfy.exception.CommandArgumentException;
import de.rojetto.comfy.exception.CommandHandlerException;
import de.rojetto.comfy.exception.CommandPathException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class CommandManager {
    private final CommandRoot root;
    private final List<CommandListener> listeners;

    public CommandManager() {
        this.root = new CommandRoot();

        this.listeners = new ArrayList<>();
    }

    public void addCommand(CommandNode commandNode) {
        root.child(commandNode);
    }

    public void addListener(CommandListener listener) {
        listeners.add(listener);
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

    protected abstract CommandContext buildContext(CommandSender sender, CommandPath path, Arguments arguments);

    public abstract void registerCommands();
}

package de.rojetto.comfy;

import de.rojetto.comfy.exception.CommandArgumentException;
import de.rojetto.comfy.exception.CommandPathException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CommandManager {
    private final CommandNode root;
    private final List<CommandListener> listeners;

    public CommandManager() {
        this.root = new CommandNode() {
            @Override
            public boolean matches(String segmentString) {
                return false;
            }
        };

        this.listeners = new ArrayList<>();
    }

    public void registerCommand(CommandNode commandNode) {
        root.child(commandNode);
    }

    private List<CommandNode> getCommandPath(String[] segments, boolean returnIncompletePath) throws CommandPathException {
        List<CommandNode> path = new ArrayList<>();
        CommandNode currentNode = root;
        CommandNode nextNode;

        for (int i = 0; i < segments.length; i++) {
            String segment = segments[i];
            nextNode = null;

            for (CommandNode child : currentNode.getChildren()) {
                if (child.matches(segment)) {
                    nextNode = child;
                    break;
                }
            }

            if (nextNode == null) {
                if (returnIncompletePath) {
                    return path;
                } else {
                    throw new CommandPathException(segment + " doesn't match any child node.");
                }
            }

            path.add(nextNode);
            currentNode = nextNode;
        }

        return path;
    }

    private Command parseCommandString(CommandSender sender, String commandString) throws CommandPathException, CommandArgumentException {
        String[] segments = commandString.split(" ");

        List<CommandNode> path = getCommandPath(segments, false);
        Map<String, Object> argumentMap = new HashMap<>();

        for (int i = 0; i < segments.length; i++) {
            CommandNode node = path.get(i);

            if (node instanceof CommandArgument) {
                CommandArgument argument = (CommandArgument) node;

                String segment = "";

                if (node.getChildren().size() == 0) {
                    for (int j = i; j < segments.length; j++) {
                        segment += segments[j];
                        if (j < segments.length - 1) {
                            segment += " ";
                        }
                    }
                } else {
                    segment = segments[i];
                }

                Object argumentValue = argument.process(segment);
                argumentMap.put(argument.getName(), argumentValue);
            }
        }

        return new Command(sender, new Arguments(argumentMap));
    }

    protected void process(CommandSender sender, String commandString) {
        Command command;

        try {
            command = parseCommandString(sender, commandString);
            if (command != null)
                callHandlerMethod(command);
        } catch (CommandPathException e) {
            sender.warning(e.getMessage());
        } catch (CommandArgumentException e) {
            sender.warning(e.getMessage());
        }
    }

    private void callHandlerMethod(Command command) {

    }

    public void addListener(CommandListener listener) {
        listeners.add(listener);
    }

    public abstract void registerCommands();
}

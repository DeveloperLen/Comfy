package de.rojetto.comfy;

import de.rojetto.comfy.exception.CommandArgumentException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CommandManager {
    private final CommandNode root;
    private final List<CommandListener> listeners;

    public CommandManager() {
        this.root = new CommandNode("root", null);
        this.listeners = new ArrayList<>();
    }

    public CommandNode commands() {
        return root;
    }

    protected Command parseCommandString(CommandSender sender, String commandString) {
        String[] chunks = commandString.split(" ");
        List<CommandNode> nodes = new ArrayList<>();
        int chunkIndex = 0;
        CommandNode currentNode = root;

        while (!currentNode.isEnd()) {
            currentNode = currentNode.getChildByLabel(chunks[chunkIndex]);
            if (currentNode == null) {
                sender.warning("Invalid subcommand: " + chunks[chunkIndex]);
                return null;
            }
            nodes.add(currentNode);
            chunkIndex += currentNode.getRequiredArguments().size() + 1;

            if (!currentNode.isEnd() && chunkIndex >= chunks.length) {
                sender.warning("Unexpected end of command");
                return null;
            }
        }

        Map<String, Object> argumentMap = new HashMap<>();

        chunkIndex = 0;
        for (CommandNode node : nodes) {
            chunkIndex++;

            for (CommandArgument argument : node.getRequiredArguments()) {
                if (chunkIndex >= chunks.length) {
                    sender.warning("Not enough arguments");
                    return null;
                }

                Object parsed;

                try {
                    parsed = argument.parse(chunks[chunkIndex]);
                } catch (CommandArgumentException e) {
                    sender.warning("Error in argument " + argument.getName() + ":");
                    sender.warning(e.getMessage());

                    return null;
                }

                argumentMap.put(argument.getName(), parsed);
                chunkIndex++;
            }
        }

        Arguments arguments = new Arguments(argumentMap);
        Command command = new Command(sender, arguments);

        return command;
    }

    public void addListener(CommandListener listener) {
        listeners.add(listener);
    }

    public abstract void registerCommands();
}

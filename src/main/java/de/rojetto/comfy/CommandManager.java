package de.rojetto.comfy;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    private final CommandNode root;
    private final List<CommandListener> listeners;

    public CommandManager() {
        this.root = new CommandNode("root", null);
        this.listeners = new ArrayList<>();
    }

    public CommandNode commandRoot() {
        return root;
    }

    public void call(String command) {
        String[] chunks = command.split(" ");
        List<CommandNode> nodes = new ArrayList<>();
        int chunkIndex = 0;
        CommandNode currentNode = root;

        while (!currentNode.isEnd()) {
            currentNode = currentNode.getChildByLabel(chunks[chunkIndex]);
            if (currentNode == null) {
                System.out.println("Invalid subcommand: " + chunks[chunkIndex]);
                break;
            }
            nodes.add(currentNode);
            chunkIndex += currentNode.getRequiredArguments().size() + 1;

            if (!currentNode.isEnd() && chunkIndex >= chunks.length) {
                System.out.println("Unexpected end of command");
                break;
            }
        }

        String pattern = "";
        for (CommandNode node : nodes)
            pattern += node + " ";

        System.out.println(pattern);
    }

    public void addListener(CommandListener listener) {
        listeners.add(listener);
    }
}

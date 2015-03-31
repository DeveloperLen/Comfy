package de.rojetto.comfy;

import de.rojetto.comfy.exception.CommandCreationException;

import java.util.ArrayList;
import java.util.List;

public abstract class CommandNode {
    private CommandNode parent;
    private List<CommandNode> children = new ArrayList<>();
    private String executes;

    public CommandNode child(CommandNode child) {
        if (child.getParent() != null)
            throw new CommandCreationException("This node already has a parent");

        child.parent = this;
        this.children.add(child);

        return this;
    }

    public boolean isOptional() {
        if (executes != null)
            return false;

        for (CommandNode child : children) {
            if (!child.isOptional()) {
                return false;
            }
        }

        return true;
    }

    public CommandNode executes(String commandHandler) {
        this.executes = commandHandler;

        return this;
    }

    public String getCommandHandler() {
        CommandNode currentNode = this;

        while (currentNode.executes == null) {
            currentNode = currentNode.getParent();
        }

        return currentNode.executes;
    }

    public abstract boolean matches(String segmentString);

    public CommandNode getParent() {
        return parent;
    }

    public List<CommandNode> getChildren() {
        return children;
    }
}

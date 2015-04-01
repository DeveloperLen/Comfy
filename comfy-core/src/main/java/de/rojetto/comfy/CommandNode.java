package de.rojetto.comfy;

import de.rojetto.comfy.exception.CommandCreationException;
import de.rojetto.comfy.exception.CommandPathException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class CommandNode {
    private CommandNode parent;
    private List<CommandNode> children = new ArrayList<>();
    private String executes;

    public CommandNode child(CommandNode child) {
        if (child.getParent() != null) {
            throw new CommandCreationException("This node already has a parent");
        }

        child.parent = this;
        this.children.add(child);

        return this;
    }

    public CommandNode executes(String commandHandler) {
        this.executes = commandHandler;

        return this;
    }

    protected CommandPath parsePath(List<String> segments, boolean returnIncompletePath) throws CommandPathException {
        List<String> segmentsCopy = new ArrayList<>(segments);

        if (segmentsCopy.size() == 0) {
            return new CommandPath(new ArrayList<CommandNode>());
        }

        CommandPath path = new CommandPath(new ArrayList<CommandNode>());
        boolean matchedChild = false;

        for (CommandNode child : children) {
            StringBuilder segment = new StringBuilder();
            if (child.getChildren().size() == 0) {
                for (int i = 0; i < segments.size(); i++) {
                    segment.append(segments.get(i));
                    if (i < segments.size() - 1) {
                        segment.append(" ");
                    }
                }
            } else {
                segment.append(segments.get(0));
            }

            if (child.matches(segment.toString())) {
                segmentsCopy.remove(0);
                path.getNodeList().add(child);
                path.getNodeList().addAll(child.parsePath(segmentsCopy, returnIncompletePath).getNodeList());
                matchedChild = true;
                break;
            }
        }

        if (!matchedChild) {
            throw new CommandPathException("Couldn't match " + segments.get(0) + " to any child node.");
        }

        return path;
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

    public boolean isExecutable() {
        return executes != null;
    }

    public String getExecutor() {
        if (executes == null) {
            return parent.getExecutor();
        }

        return executes;
    }

    public CommandPath getPath() {
        List<CommandNode> nodeList = new ArrayList<>();
        CommandNode currentNode = this;

        while (!(currentNode instanceof CommandRoot)) {
            nodeList.add(currentNode);
            currentNode = currentNode.getParent();
        }

        Collections.reverse(nodeList);

        return new CommandPath(nodeList);
    }

    public abstract boolean matches(String segmentString);

    public CommandNode getParent() {
        return parent;
    }

    public List<CommandNode> getChildren() {
        return new ArrayList<>(children);
    }
}

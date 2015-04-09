package me.rojetto.comfy.tree;

import me.rojetto.comfy.CommandContext;
import me.rojetto.comfy.exception.CommandPathException;
import me.rojetto.comfy.exception.CommandTreeException;

import java.util.*;

public abstract class CommandNode {
    private CommandNode parent;
    private List<CommandNode> children = new ArrayList<>();
    private Map<String, String> tags = new HashMap<>();

    public CommandNode child(CommandNode child) {
        if (child.getParent() != null) {
            throw new CommandTreeException("This node already has a parent");
        }

        child.parent = this;
        this.children.add(child);

        return this;
    }

    public CommandNode executes(String handler) {
        tags.put("handler", handler);

        return this;
    }

    public CommandNode description(String description) {
        tags.put("description", description);

        return this;
    }

    public CommandNode permission(String permission) {
        tags.put("permission", permission);

        return this;
    }

    public CommandPath parsePath(List<String> segments, boolean returnIncompletePath) throws CommandPathException {
        List<String> segmentsCopy = new ArrayList<>(segments);

        if (segmentsCopy.size() == 0) {
            return new CommandPath(new ArrayList<CommandNode>());
        }

        CommandPath path = new CommandPath(new ArrayList<CommandNode>());
        boolean matchedChild = false;

        for (CommandNode child : children) {
            StringBuilder segment = new StringBuilder();
            boolean lastNode;

            if (child.isLeaf() && child instanceof CommandArgument) { // If this is a leaf argument, give it all of the remaining segments
                for (int i = 0; i < segments.size(); i++) {
                    segment.append(segments.get(i));
                    if (i < segments.size() - 1) {
                        segment.append(" ");
                    }
                }

                lastNode = true;
            } else {
                segment.append(segments.get(0));
                lastNode = false;
            }

            if (child.matches(segment.toString())) {
                if (lastNode) {
                    segmentsCopy.clear();
                } else {
                    segmentsCopy.remove(0);
                }
                path.getNodeList().add(child);
                path.getNodeList().addAll(child.parsePath(segmentsCopy, returnIncompletePath).getNodeList());
                matchedChild = true;
                break;
            }
        }

        if (!matchedChild && !returnIncompletePath) {
            throw new CommandPathException("Invalid sub-command or argument: '" + segments.get(0) + "'");
        }

        return path;
    }

    public boolean hasTag(String tag) {
        return tags.containsKey(tag);
    }

    public String getTag(String tag) {
        return tags.get(tag);
    }

    public List<CommandNode> getNodesWithTag(String tag, boolean deep) {
        List<CommandNode> nodes = new ArrayList<>();

        if (hasTag(tag)) {
            nodes.add(this);
        }

        if (deep || !hasTag(tag)) {
            for (CommandNode child : children) {
                nodes.addAll(child.getNodesWithTag(tag, deep));
            }
        }

        return nodes;
    }

    public boolean hasDescription() {
        return hasTag("description");
    }

    public String getDescription() {
        return getTag("description");
    }

    public boolean hasPermission() {
        return hasTag("permission");
    }

    public String getPermission() {
        return getTag("permission");
    }

    public boolean isOptional() {
        if (hasTag("handler"))
            return false;

        for (CommandNode child : children) {
            if (!child.isOptional()) {
                return false;
            }
        }

        return true;
    }

    public boolean isExecutable() {
        if (hasTag("handler")) {
            return true;
        } else {
            if (isOptional()) {
                return parent.isExecutable();
            } else {
                return false;
            }
        }
    }

    public boolean isLastExecutable() {
        return getNodesWithTag("handler", true).size() == 1 && getNodesWithTag("handler", true).get(0) == this;
    }

    public boolean isLeaf() {
        return children.size() == 0;
    }

    public String getHandler() {
        if (isExecutable()) {
            if (hasTag("handler")) {
                return getTag("handler");
            } else {
                return parent.getHandler();
            }
        } else {
            return null;
        }
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

    public List<CommandNode> getLeafNodes() {
        List<CommandNode> leafNodes = new ArrayList<>();

        if (isLeaf()) {
            leafNodes.add(this);
        } else {
            for (CommandNode child : children) {
                leafNodes.addAll(child.getLeafNodes());
            }
        }

        return leafNodes;
    }

    public List<CommandNode> getExecutableNodes(boolean deep) {
        return getNodesWithTag("handler", deep);
    }

    public CommandNode getLastOptional() {
        if (isLeaf()) {
            return this;
        }

        if (isLastExecutable() || isOptional()) {
            return getLeafNodes().get(0);
        }

        return this;
    }

    public boolean hasChild(CommandNode node) {
        for (CommandNode child : children) {
            if (child == node) {
                return true;
            }

            if (child.hasChild(node)) {
                return true;
            }
        }

        return false;
    }

    public boolean isCategory() {
        return !isExecutable() && hasDescription();
    }

    public CommandNode getParent() {
        return parent;
    }

    public List<CommandNode> getChildren() {
        return new ArrayList<>(children);
    }

    public abstract boolean matches(String segment);

    public abstract List<String> getSuggestions(CommandContext context);
}

package me.rojetto.comfy.tree;

import me.rojetto.comfy.Arguments;
import me.rojetto.comfy.CommandSender;
import me.rojetto.comfy.Flag;
import me.rojetto.comfy.exception.ArgumentException;
import me.rojetto.comfy.exception.ArgumentParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandPath implements Comparable<CommandPath> {
    private final List<CommandNode> nodeList;

    protected CommandPath(List<CommandNode> nodeList) {
        this.nodeList = nodeList;
    }

    public Arguments parseArguments(List<String> segments) throws ArgumentException {
        Map<String, Object> argumentMap = new HashMap<>();

        for (int i = 0; i < segments.size(); i++) {
            StringBuilder segment = new StringBuilder();
            boolean lastNode;

            if (i == nodeList.size() - 1) { // If this is an end point, give it all of the remaining segments
                for (int j = i; j < segments.size(); j++) {
                    segment.append(segments.get(j));
                    if (j < segments.size() - 1) {
                        segment.append(" ");
                    }
                }

                lastNode = true;
            } else {
                segment.append(segments.get(i));
                lastNode = false;
            }

            if (nodeList.get(i) instanceof Argument) {
                Argument argument = (Argument) nodeList.get(i);
                Object o;
                try {
                    o = argument.getType().parse(segment.toString());
                } catch (ArgumentParseException e) {
                    throw new ArgumentException(argument.getName(), e.getMessage());
                }
                argumentMap.put(argument.getName(), o);
            }

            if (lastNode) {
                break;
            }
        }

        return new Arguments(argumentMap);
    }

    public CommandNode getLastNode() {
        if (nodeList.size() > 0) {
            return nodeList.get(nodeList.size() - 1);
        } else {
            return null;
        }
    }

    public List<CommandNode> getNodeList() {
        return nodeList;
    }

    public List<Flag> getFlags() {
        List<Flag> flags = new ArrayList<>();

        for (CommandNode node : nodeList) {
            flags.addAll(node.getFlags());
        }

        return flags;
    }

    public boolean checkPermission(CommandSender sender) {
        for (int i = nodeList.size() - 1; i >= 0; i--) {
            if (nodeList.get(i).hasPermission()) {
                return sender.hasPermission(nodeList.get(i).getPermission());
            }
        }

        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < nodeList.size(); i++) {
            builder.append(nodeList.get(i).toString());

            if (i < nodeList.size() - 1) {
                builder.append(" ");
            }
        }

        return builder.toString();
    }

    @Override
    public int compareTo(CommandPath o) {
        if (o.getLastNode() == getLastNode()) {
            return 0;
        }

        for (int i = 0; i < Math.min(nodeList.size(), o.getNodeList().size()); i++) {
            CommandNode myNode = nodeList.get(i);
            CommandNode otherNode = o.getNodeList().get(i);

            if (myNode != otherNode) {
                if (!myNode.isCategory() && otherNode.isCategory()) {
                    return 1;
                } else if (myNode.isCategory() && !otherNode.isCategory()) {
                    return -1;
                } else {
                    return myNode.getParent().getChildren().indexOf(myNode) - otherNode.getParent().getChildren().indexOf(otherNode);
                }
            }

            if (i == Math.min(nodeList.size(), o.getNodeList().size()) - 1) {
                return nodeList.size() - o.getNodeList().size();
            }
        }

        return -1;
    }
}

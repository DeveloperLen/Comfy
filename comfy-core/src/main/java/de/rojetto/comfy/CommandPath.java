package de.rojetto.comfy;

import de.rojetto.comfy.exception.CommandArgumentException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandPath {
    private final List<CommandNode> nodeList;

    protected CommandPath(List<CommandNode> nodeList) {
        this.nodeList = nodeList;
    }

    protected Arguments parseArguments(List<String> segments) throws CommandArgumentException {
        Map<String, Object> argumentMap = new HashMap<>();

        for (int i = 0; i < segments.size(); i++) {
            StringBuilder segment = new StringBuilder();

            if (i == nodeList.size() - 1) { // If this is an end point, give it all of the remaining segments
                for (int j = i; j < segments.size(); j++) {
                    segment.append(segments.get(j));
                    if (j < segments.size() - 1) {
                        segment.append(" ");
                    }
                }
            } else {
                segment.append(segments.get(i));
            }

            if (nodeList.get(i) instanceof CommandArgument) {
                CommandArgument argument = (CommandArgument) nodeList.get(i);
                Object o = argument.parse(segment.toString());
                argumentMap.put(argument.getName(), o);
            }
        }

        return new Arguments(argumentMap);
    }

    public CommandNode getLastNode() {
        return nodeList.get(nodeList.size() - 1);
    }

    public List<CommandNode> getNodeList() {
        return nodeList;
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
}

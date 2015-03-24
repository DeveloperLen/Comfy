package de.rojetto.comfy;

import de.rojetto.comfy.exception.CommandCreationException;

import java.util.ArrayList;
import java.util.List;

public class CommandNode {
    private final String label;
    private final CommandNode parent;
    private final List<CommandArgument> requiredArguments;
    private final List<CommandArgument> optionalArguments;
    private final List<CommandNode> children;

    protected CommandNode(String label, CommandNode parent) {
        this.label = label;
        this.parent = parent;
        this.requiredArguments = new ArrayList<>();
        this.optionalArguments = new ArrayList<>();
        this.children = new ArrayList<>();
    }

    public CommandNode child(String label) throws CommandCreationException {
        if (getChildByLabel(label) != null)
            throw new CommandCreationException("Subcommand " + label + " already exists.");

        CommandNode child = new CommandNode(label, this);
        this.children.add(child);

        return child;
    }

    public CommandNode required(CommandArgument argument) {
        this.requiredArguments.add(argument);

        return this;
    }

    public CommandNode optional(CommandArgument argument) {
        this.optionalArguments.add(argument);

        return this;
    }

    public String getLabel() {
        return label;
    }

    public CommandNode getParent() {
        return parent;
    }

    public boolean isRoot() {
        return parent == null;
    }

    public boolean isEnd() {
        return children.size() == 0;
    }

    public CommandNode getChildByLabel(String label) {
        for (CommandNode child : children) {
            if (child.getLabel().equalsIgnoreCase(label))
                return child;
        }

        return null;
    }

    public List<CommandArgument> getRequiredArguments() {
        return new ArrayList<>(requiredArguments);
    }

    public List<CommandArgument> getOptionalArguments() {
        return new ArrayList<>(optionalArguments);
    }

    public List<CommandNode> getChildren() {
        return new ArrayList<>(children);
    }

    @Override
    public String toString() {
        String string = label;

        for (CommandArgument argument : requiredArguments)
            string += " <" + argument.getName() + ">";

        for (CommandArgument argument : optionalArguments)
            string += " [" + argument.getName() + "]";

        return string;
    }
}

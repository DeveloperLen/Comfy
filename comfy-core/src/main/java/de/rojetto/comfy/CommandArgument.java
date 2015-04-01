package de.rojetto.comfy;

import de.rojetto.comfy.exception.CommandArgumentException;

public abstract class CommandArgument extends CommandNode {
    private final String name;

    protected CommandArgument(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        if (isOptional()) {
            return "<" + name + ">";
        } else {
            return "[" + name + "]";
        }
    }

    public String getName() {
        return name;
    }

    abstract protected Object parse(String argument) throws CommandArgumentException;
}

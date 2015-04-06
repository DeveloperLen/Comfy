package me.rojetto.comfy;

import me.rojetto.comfy.exception.CommandArgumentException;
import me.rojetto.comfy.exception.CommandTreeException;

public abstract class CommandArgument extends CommandNode {
    private final String name;

    protected CommandArgument(String name) {
        this.name = name;

        if (!name.matches("[A-Za-z0-9]+")) {
            throw new CommandTreeException("Argument names can only contain alphanumeric characters.");
        }
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

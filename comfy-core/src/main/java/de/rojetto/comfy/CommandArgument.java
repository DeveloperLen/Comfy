package de.rojetto.comfy;

import de.rojetto.comfy.exception.CommandArgumentException;

public abstract class CommandArgument extends CommandNode {
    private final String name;

    protected CommandArgument(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    abstract protected Object process(String argument) throws CommandArgumentException;
}

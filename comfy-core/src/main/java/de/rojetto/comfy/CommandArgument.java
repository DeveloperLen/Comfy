package de.rojetto.comfy;

import de.rojetto.comfy.exception.CommandArgumentException;

public abstract class CommandArgument {
    private final String name;

    protected CommandArgument(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    abstract protected Object parse(String argument) throws CommandArgumentException;
}

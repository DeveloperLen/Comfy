package de.rojetto.comfy.argument;

import de.rojetto.comfy.CommandArgument;
import de.rojetto.comfy.exception.CommandArgumentException;

public class StringArgument extends CommandArgument {
    public StringArgument(String name) {
        super(name);
    }

    @Override
    protected Object parse(String argument) throws CommandArgumentException {
        return argument;
    }
}

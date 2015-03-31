package de.rojetto.comfy.argument;

import de.rojetto.comfy.CommandArgument;
import de.rojetto.comfy.exception.CommandArgumentException;

public class StringArgument extends CommandArgument {
    public StringArgument(String name) {
        super(name);
    }

    @Override
    protected Object process(String argument) throws CommandArgumentException {
        return argument;
    }

    @Override
    public boolean matches(String segmentString) {
        return true;
    }
}

package me.rojetto.comfy.argument;

import me.rojetto.comfy.CommandArgument;
import me.rojetto.comfy.exception.CommandArgumentException;

public class StringArgument extends CommandArgument {
    public StringArgument(String name) {
        super(name);
    }

    @Override
    protected Object parse(String argument) throws CommandArgumentException {
        return argument;
    }

    @Override
    public boolean matches(String segmentString) {
        return true;
    }
}

package me.rojetto.comfy.argument;

import me.rojetto.comfy.CommandContext;
import me.rojetto.comfy.exception.CommandArgumentException;

import java.util.List;

public class IntegerArgument extends RangedNumberArgument<Integer> {
    public IntegerArgument(String name) {
        super(name);
    }

    @Override
    protected Integer parse(String argument) throws CommandArgumentException {
        int integer;

        try {
            integer = Integer.parseInt(argument);
        } catch (NumberFormatException e) {
            throw new CommandArgumentException(argument + " is not a valid integer.");
        }

        checkRange(integer);
        return integer;
    }

    @Override
    public boolean matches(String segmentString) {
        try {
            Integer.parseInt(segmentString);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    @Override
    public List<String> getSuggestions(CommandContext context) {
        return null;
    }
}

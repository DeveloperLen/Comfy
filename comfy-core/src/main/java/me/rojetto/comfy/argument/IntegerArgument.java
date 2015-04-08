package me.rojetto.comfy.argument;

import me.rojetto.comfy.CommandContext;
import me.rojetto.comfy.exception.CommandArgumentException;

import java.util.List;

public class IntegerArgument extends RangedNumberArgument<Integer> {
    public IntegerArgument(String name) {
        super(name);
    }

    @Override
    public Integer parse(String segment) throws CommandArgumentException {
        int integer;

        try {
            integer = Integer.parseInt(segment);
        } catch (NumberFormatException e) {
            throw new CommandArgumentException("'" + segment + "' is not a valid integer.");
        }

        checkRange(integer);
        return integer;
    }

    @Override
    public boolean matches(String segment) {
        try {
            Integer.parseInt(segment);
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

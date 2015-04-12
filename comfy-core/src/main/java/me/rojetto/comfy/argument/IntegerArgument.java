package me.rojetto.comfy.argument;

import me.rojetto.comfy.exception.CommandArgumentParseException;

public class IntegerArgument extends RangedNumberArgument<Integer> {
    @Override
    public Integer parse(String segment) throws CommandArgumentParseException {
        int integer;

        try {
            integer = Integer.parseInt(segment);
        } catch (NumberFormatException e) {
            throw new CommandArgumentParseException("'" + segment + "' is not a valid integer.");
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
}

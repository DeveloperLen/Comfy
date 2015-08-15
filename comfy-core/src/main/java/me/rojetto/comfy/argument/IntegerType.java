package me.rojetto.comfy.argument;

import me.rojetto.comfy.exception.ArgumentParseException;

public class IntegerType extends RangedNumberType<Integer> {
    @Override
    public Integer parse(String segment) throws ArgumentParseException {
        int integer;

        try {
            integer = Integer.parseInt(segment);
        } catch (NumberFormatException e) {
            throw new ArgumentParseException("'" + segment + "' is not a valid integer.");
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

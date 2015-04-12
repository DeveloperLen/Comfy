package me.rojetto.comfy.argument;

import me.rojetto.comfy.exception.CommandArgumentParseException;

public class FloatArgument extends RangedNumberArgument<Float> {
    @Override
    public Float parse(String segment) throws CommandArgumentParseException {
        float number;

        try {
            number = Float.parseFloat(segment);
        } catch (NumberFormatException e) {
            throw new CommandArgumentParseException("'" + segment + "' is not a valid floating point number.");
        }

        checkRange(number);
        return number;
    }

    @Override
    public boolean matches(String segment) {
        try {
            Float.parseFloat(segment);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }
}

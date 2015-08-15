package me.rojetto.comfy.argument;

import me.rojetto.comfy.exception.ArgumentParseException;

public class FloatType extends RangedNumberType<Float> {
    @Override
    public Float parse(String segment) throws ArgumentParseException {
        float number;

        try {
            number = Float.parseFloat(segment);
        } catch (NumberFormatException e) {
            throw new ArgumentParseException("'" + segment + "' is not a valid floating point number.");
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

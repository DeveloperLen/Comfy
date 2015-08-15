package me.rojetto.comfy.argument;

import me.rojetto.comfy.exception.ArgumentParseException;

public class DoubleType extends RangedNumberType<Double> {
    @Override
    public Double parse(String segment) throws ArgumentParseException {
        double number;

        try {
            number = Double.parseDouble(segment);
        } catch (NumberFormatException e) {
            throw new ArgumentParseException("'" + segment + "' is not a valid floating point number.");
        }

        checkRange(number);
        return number;
    }

    @Override
    public boolean matches(String segment) {
        try {
            Double.parseDouble(segment);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }
}

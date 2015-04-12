package me.rojetto.comfy.argument;

import me.rojetto.comfy.exception.CommandArgumentParseException;

public class DoubleType extends RangedNumberType<Double> {
    @Override
    public Double parse(String segment) throws CommandArgumentParseException {
        double number;

        try {
            number = Double.parseDouble(segment);
        } catch (NumberFormatException e) {
            throw new CommandArgumentParseException("'" + segment + "' is not a valid floating point number.");
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

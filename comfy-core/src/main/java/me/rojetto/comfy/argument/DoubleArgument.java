package me.rojetto.comfy.argument;

import me.rojetto.comfy.CommandContext;
import me.rojetto.comfy.exception.CommandArgumentException;

import java.util.List;

public class DoubleArgument extends RangedNumberArgument<Double> {
    public DoubleArgument(String name) {
        super(name);
    }

    @Override
    public Double parse(String segment) throws CommandArgumentException {
        double number;

        try {
            number = Double.parseDouble(segment);
        } catch (NumberFormatException e) {
            throw new CommandArgumentException("'" + segment + "' is not a valid floating point number.");
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

    @Override
    public List<String> getSuggestions(CommandContext context) {
        return null;
    }
}

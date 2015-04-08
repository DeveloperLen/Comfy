package me.rojetto.comfy.argument;

import me.rojetto.comfy.CommandContext;
import me.rojetto.comfy.exception.CommandArgumentException;

import java.util.List;

public class FloatArgument extends RangedNumberArgument<Float> {
    public FloatArgument(String name) {
        super(name);
    }

    @Override
    public Float parse(String segment) throws CommandArgumentException {
        float number;

        try {
            number = Float.parseFloat(segment);
        } catch (NumberFormatException e) {
            throw new CommandArgumentException("'" + segment + "' is not a valid floating point number.");
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

    @Override
    public List<String> getSuggestions(CommandContext context) {
        return null;
    }
}

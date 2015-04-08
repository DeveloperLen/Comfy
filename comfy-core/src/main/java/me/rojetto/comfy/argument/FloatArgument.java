package me.rojetto.comfy.argument;

import me.rojetto.comfy.CommandContext;
import me.rojetto.comfy.exception.CommandArgumentException;

import java.util.List;

public class FloatArgument extends RangedNumberArgument<Float> {
    public FloatArgument(String name) {
        super(name);
    }

    @Override
    protected Float parse(String argument) throws CommandArgumentException {
        float number;

        try {
            number = Float.parseFloat(argument);
        } catch (NumberFormatException e) {
            throw new CommandArgumentException(argument + " is not a valid floating point number.");
        }

        checkRange(number);
        return number;
    }

    @Override
    public boolean matches(String segmentString) {
        try {
            Float.parseFloat(segmentString);
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

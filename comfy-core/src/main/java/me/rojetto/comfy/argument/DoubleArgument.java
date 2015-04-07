package me.rojetto.comfy.argument;

import me.rojetto.comfy.CommandContext;
import me.rojetto.comfy.exception.CommandArgumentException;

import java.util.List;

public class DoubleArgument extends RangedNumberArgument {
    public DoubleArgument(String name) {
        super(name);
    }

    @Override
    protected Object parse(String argument) throws CommandArgumentException {
        double number;

        try {
            number = Double.parseDouble(argument);
        } catch (NumberFormatException e) {
            throw new CommandArgumentException(argument + " is not a valid floating point number.");
        }

        checkRange(number);
        return number;
    }

    @Override
    public boolean matches(String segmentString) {
        try {
            Double.parseDouble(segmentString);
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

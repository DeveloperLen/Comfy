package de.rojetto.comfy.argument;

import de.rojetto.comfy.CommandArgument;
import de.rojetto.comfy.exception.CommandArgumentException;

public class IntegerArgument extends CommandArgument {
    private boolean checkMin;
    private int min;
    private boolean checkMax;
    private int max;

    public IntegerArgument(String name) {
        super(name);

        this.checkMin = false;
        this.min = 0;
        this.checkMax = false;
        this.max = 0;
    }

    public IntegerArgument min(int min) {
        this.checkMin = true;
        this.min = min;

        return this;
    }

    public IntegerArgument max(int max) {
        this.checkMax = true;
        this.max = max;

        return this;
    }

    @Override
    protected Object parse(String argument) throws CommandArgumentException {
        int integer;

        try {
            integer = Integer.parseInt(argument);
        } catch (NumberFormatException e) {
            throw new CommandArgumentException(argument + " is not a valid integer.");
        }

        if (checkMin && integer < min)
            throw new CommandArgumentException(getName() + " must be greater than " + min);

        if (checkMax && integer > max)
            throw new CommandArgumentException(getName() + " must be smaller than " + max);

        return integer;
    }
}

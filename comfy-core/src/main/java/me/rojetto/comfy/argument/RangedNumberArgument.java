package me.rojetto.comfy.argument;

import me.rojetto.comfy.ArgumentType;
import me.rojetto.comfy.exception.CommandArgumentParseException;

public abstract class RangedNumberArgument<T extends Number> extends ArgumentType<Number> {
    private boolean checkMin;
    private double min;
    private boolean checkMax;
    private double max;

    protected RangedNumberArgument() {
        this.checkMin = false;
        this.min = 0;
        this.checkMax = false;
        this.max = 0;
    }

    public RangedNumberArgument min(double min) {
        this.checkMin = true;
        this.min = min;

        return this;
    }

    public RangedNumberArgument max(double max) {
        this.checkMax = true;
        this.max = max;

        return this;
    }

    public boolean checkRange(double number) throws CommandArgumentParseException {
        if (checkMin && number < min)
            throw new CommandArgumentParseException("Value must be greater than " + min);

        if (checkMax && number > max)
            throw new CommandArgumentParseException("Value must be smaller than " + max);

        return true;
    }
}

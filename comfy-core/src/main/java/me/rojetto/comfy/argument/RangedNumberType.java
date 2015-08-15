package me.rojetto.comfy.argument;

import me.rojetto.comfy.ArgumentType;
import me.rojetto.comfy.exception.ArgumentParseException;

public abstract class RangedNumberType<T extends Number> extends ArgumentType<Number> {
    private boolean checkMin;
    private double min;
    private boolean checkMax;
    private double max;

    protected RangedNumberType() {
        this.checkMin = false;
        this.min = 0;
        this.checkMax = false;
        this.max = 0;
    }

    public RangedNumberType min(double min) {
        this.checkMin = true;
        this.min = min;

        return this;
    }

    public RangedNumberType max(double max) {
        this.checkMax = true;
        this.max = max;

        return this;
    }

    public boolean checkRange(double number) throws ArgumentParseException {
        if (checkMin && number < min)
            throw new ArgumentParseException("Value must be greater than " + min);

        if (checkMax && number > max)
            throw new ArgumentParseException("Value must be smaller than " + max);

        return true;
    }
}

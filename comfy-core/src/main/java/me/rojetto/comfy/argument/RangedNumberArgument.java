package me.rojetto.comfy.argument;

import me.rojetto.comfy.exception.CommandArgumentException;
import me.rojetto.comfy.tree.CommandArgument;

public abstract class RangedNumberArgument extends CommandArgument {
    private boolean checkMin;
    private double min;
    private boolean checkMax;
    private double max;

    protected RangedNumberArgument(String name) {
        super(name);

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

    public boolean checkRange(double number) throws CommandArgumentException {
        if (checkMin && number < min)
            throw new CommandArgumentException(getName() + " must be greater than " + min);

        if (checkMax && number > max)
            throw new CommandArgumentException(getName() + " must be smaller than " + max);

        return true;
    }
}

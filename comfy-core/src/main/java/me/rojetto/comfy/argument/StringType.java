package me.rojetto.comfy.argument;

import me.rojetto.comfy.ArgumentType;
import me.rojetto.comfy.exception.ArgumentParseException;

public class StringType extends ArgumentType<String> {
    @Override
    public String parse(String segment) throws ArgumentParseException {
        return segment;
    }

    @Override
    public boolean matches(String segment) {
        return segment.matches(".+");
    }
}

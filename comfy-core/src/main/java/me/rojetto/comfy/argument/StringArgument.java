package me.rojetto.comfy.argument;

import me.rojetto.comfy.ArgumentType;
import me.rojetto.comfy.exception.CommandArgumentParseException;

public class StringArgument extends ArgumentType<String> {
    @Override
    public String parse(String segment) throws CommandArgumentParseException {
        return segment;
    }

    @Override
    public boolean matches(String segment) {
        return segment.matches(".+");
    }
}

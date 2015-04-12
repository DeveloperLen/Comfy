package me.rojetto.comfy;

import me.rojetto.comfy.exception.CommandArgumentParseException;

import java.util.List;

public abstract class ArgumentType<T> {
    abstract public T parse(String segment) throws CommandArgumentParseException;

    public boolean matches(String segment) {
        try {
            parse(segment);
        } catch (CommandArgumentParseException e) {
            return false;
        }

        return true;
    }

    public List<String> getSuggestions(CommandContext context) {
        return null;
    }
}

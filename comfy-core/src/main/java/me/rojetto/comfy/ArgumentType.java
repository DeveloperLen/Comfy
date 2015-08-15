package me.rojetto.comfy;

import me.rojetto.comfy.exception.ArgumentParseException;

import java.util.List;

public abstract class ArgumentType<T> {
    abstract public T parse(String segment) throws ArgumentParseException;

    public boolean matches(String segment) {
        try {
            parse(segment);
        } catch (ArgumentParseException e) {
            return false;
        }

        return true;
    }

    public List<String> getSuggestions(CommandContext context) {
        return null;
    }
}

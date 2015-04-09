package me.rojetto.comfy.argument;

import me.rojetto.comfy.CommandContext;
import me.rojetto.comfy.exception.CommandArgumentException;
import me.rojetto.comfy.tree.CommandArgument;

import java.util.List;

public class StringArgument extends CommandArgument<String> {
    public StringArgument(String name) {
        super(name);
    }

    @Override
    public String parse(String segment) throws CommandArgumentException {
        return segment;
    }

    @Override
    public boolean matches(String segment) {
        return segment.matches(".+");
    }

    @Override
    public List<String> getSuggestions(CommandContext context) {
        return null;
    }
}

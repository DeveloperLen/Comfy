package me.rojetto.comfy.argument;

import me.rojetto.comfy.CommandContext;
import me.rojetto.comfy.exception.CommandArgumentException;
import me.rojetto.comfy.tree.CommandArgument;

import java.util.List;

public class StringArgument extends CommandArgument {
    public StringArgument(String name) {
        super(name);
    }

    @Override
    protected Object parse(String argument) throws CommandArgumentException {
        return argument;
    }

    @Override
    public List<String> getSuggestions(CommandContext context) {
        return null;
    }
}

package me.rojetto.comfy.tree;

import me.rojetto.comfy.ArgumentType;
import me.rojetto.comfy.CommandContext;
import me.rojetto.comfy.exception.CommandTreeException;

import java.util.List;

public class Argument extends CommandNode<Argument> {
    private final String name;
    private final ArgumentType type;

    public Argument(String name, ArgumentType type) {
        this.name = name;
        this.type = type;

        if (!name.matches("[A-Za-z0-9]+")) {
            throw new CommandTreeException("Argument names can only contain alphanumeric characters.");
        }
    }

    public String getName() {
        return name;
    }

    public ArgumentType getType() {
        return type;
    }

    public boolean matches(String segment) {
        return type.matches(segment);
    }

    @Override
    public List<String> getSuggestions(CommandContext context) {
        return type.getSuggestions(context);
    }

    @Override
    public String toString() {
        if (isOptional()) {
            return "<" + name + ">";
        } else {
            return "[" + name + "]";
        }
    }
}

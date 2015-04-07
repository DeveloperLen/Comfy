package me.rojetto.comfy.tree;

import me.rojetto.comfy.CommandContext;
import me.rojetto.comfy.exception.CommandTreeException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Literal extends CommandNode {
    private final List<String> aliases;

    public Literal(String label, String... aliases) {
        this.aliases = new ArrayList<>();
        this.aliases.add(label);
        this.aliases.addAll(Arrays.asList(aliases));

        for (String alias : this.aliases) {
            if (!alias.matches("[A-Za-z0-9]+")) {
                throw new CommandTreeException("Literals can only contain alphanumeric characters.");
            }
        }
    }

    @Override
    public boolean matches(String segmentString) {
        for (String alias : aliases) {
            if (alias.equalsIgnoreCase(segmentString)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public List<String> getSuggestions(CommandContext context) {
        return Arrays.asList(aliases.get(0));
    }

    @Override
    public String toString() {
        return aliases.get(0);
    }
}

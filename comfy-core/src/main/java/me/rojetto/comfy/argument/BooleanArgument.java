package me.rojetto.comfy.argument;

import me.rojetto.comfy.CommandContext;
import me.rojetto.comfy.exception.CommandArgumentException;
import me.rojetto.comfy.tree.CommandArgument;

import java.util.*;

public class BooleanArgument extends CommandArgument<Boolean> {
    private final Map<Boolean, String[]> booleanNames;

    public BooleanArgument(String name) {
        this(name, new String[]{"true", "on", "yes", "enable", "1"}, new String[]{"false", "off", "no", "disable", "0"});
    }

    public BooleanArgument(String name, String[] trueAliases, String[] falseAliases) {
        super(name);

        this.booleanNames = new HashMap<>();
        booleanNames.put(true, trueAliases);
        booleanNames.put(false, falseAliases);
    }

    @Override
    public Boolean parse(String segment) throws CommandArgumentException {
        for (boolean key : booleanNames.keySet()) {
            for (String alias : booleanNames.get(key)) {
                if (alias.equalsIgnoreCase(segment)) {
                    return key;
                }
            }
        }

        throw new CommandArgumentException("'" + segment + "' is not a valid boolean.");
    }

    @Override
    public List<String> getSuggestions(CommandContext context) {
        List<String> suggestions = new ArrayList<>();
        suggestions.addAll(Arrays.asList(booleanNames.get(true)));
        suggestions.addAll(Arrays.asList(booleanNames.get(false)));

        return suggestions;
    }
}

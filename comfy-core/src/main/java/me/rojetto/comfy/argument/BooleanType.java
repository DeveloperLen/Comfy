package me.rojetto.comfy.argument;

import me.rojetto.comfy.ArgumentType;
import me.rojetto.comfy.CommandContext;
import me.rojetto.comfy.exception.CommandArgumentParseException;

import java.util.*;

public class BooleanType extends ArgumentType<Boolean> {
    private final Map<Boolean, String[]> booleanNames;

    public BooleanType() {
        this(new String[]{"true", "on", "yes", "enable", "1"}, new String[]{"false", "off", "no", "disable", "0"});
    }

    public BooleanType(String[] trueAliases, String[] falseAliases) {
        this.booleanNames = new HashMap<>();
        booleanNames.put(true, trueAliases);
        booleanNames.put(false, falseAliases);
    }

    @Override
    public Boolean parse(String segment) throws CommandArgumentParseException {
        for (boolean key : booleanNames.keySet()) {
            for (String alias : booleanNames.get(key)) {
                if (alias.equalsIgnoreCase(segment)) {
                    return key;
                }
            }
        }

        throw new CommandArgumentParseException("'" + segment + "' is not a valid boolean.");
    }

    @Override
    public List<String> getSuggestions(CommandContext context) {
        List<String> suggestions = new ArrayList<>();
        suggestions.addAll(Arrays.asList(booleanNames.get(true)));
        suggestions.addAll(Arrays.asList(booleanNames.get(false)));

        return suggestions;
    }
}

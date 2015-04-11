package me.rojetto.comfy.argument;

import me.rojetto.comfy.CommandContext;
import me.rojetto.comfy.exception.CommandArgumentException;
import me.rojetto.comfy.tree.CommandArgument;

import java.util.*;

public class EnumArgument<T extends Enum> extends CommandArgument<T> {
    private final Map<String, T> enumMap;

    public EnumArgument(String name, Map<String, T> enumMap) {
        super(name);

        this.enumMap = enumMap;
    }

    public EnumArgument(String name, T[] enumValues, String[] names) {
        this(name, (Map<String, T>) makeEnumMap(enumValues, names));
    }

    public static Map<String, Enum> makeEnumMap(Enum[] enumValues, String[] names) throws IllegalArgumentException {
        if (enumValues.length != names.length) {
            throw new IllegalArgumentException("The number of enum values and names has to be the same.");
        }

        Map<String, Enum> enumMap = new HashMap<>();

        for (int i = 0; i < names.length; i++) {
            enumMap.put(names[i], enumValues[i]);
        }

        return enumMap;
    }

    @Override
    public T parse(String segment) throws CommandArgumentException {
        for (String enumName : enumMap.keySet()) {
            if (enumName.equalsIgnoreCase(segment)) {
                return enumMap.get(enumName);
            }
        }

        StringBuilder options = new StringBuilder();

        Iterator<String> iter = enumMap.keySet().iterator();
        while (iter.hasNext()) {
            options.append(iter.next());
            if (iter.hasNext()) {
                options.append("|");
            }
        }

        throw new CommandArgumentException("'" + segment + "' is not a valid option. Suggestions: " + options.toString());
    }

    @Override
    public boolean matches(String segment) {
        return true;
    }

    @Override
    public List<String> getSuggestions(CommandContext context) {
        return new ArrayList<>(enumMap.keySet());
    }
}

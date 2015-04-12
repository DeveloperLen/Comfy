package me.rojetto.comfy.argument;

import me.rojetto.comfy.ArgumentType;
import me.rojetto.comfy.CommandContext;
import me.rojetto.comfy.exception.CommandArgumentParseException;

import java.util.*;

public class EnumType<T extends Enum> extends ArgumentType<T> {
    private final Map<String, T> enumMap;

    public EnumType(Map<String, T> enumMap) {
        this.enumMap = enumMap;
    }

    public EnumType(T[] enumValues, String[] names) {
        this(EnumType.makeEnumMap(enumValues, names));
    }

    public static <T extends Enum> Map<String, T> makeEnumMap(T[] enumValues, String[] names) throws IllegalArgumentException {
        if (enumValues.length != names.length) {
            throw new IllegalArgumentException("The number of enum values and names has to be the same.");
        }

        Map<String, T> enumMap = new HashMap<>();

        for (int i = 0; i < names.length; i++) {
            enumMap.put(names[i], enumValues[i]);
        }

        return enumMap;
    }

    @Override
    public T parse(String segment) throws CommandArgumentParseException {
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

        throw new CommandArgumentParseException("'" + segment + "' is not a valid option. Suggestions: " + options.toString());
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

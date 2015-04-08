package me.rojetto.comfy.argument;

import me.rojetto.comfy.CommandContext;
import me.rojetto.comfy.exception.CommandArgumentException;
import me.rojetto.comfy.tree.CommandArgument;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnumArgument extends CommandArgument {
    private final Map<String, Enum> enumMap;

    public EnumArgument(String name, Map<String, Enum> enumMap) {
        super(name);

        this.enumMap = enumMap;
    }

    public EnumArgument(String name, Enum[] enumValues, String[] names) {
        this(name, EnumArgument.makeEnumMap(enumValues, names));
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
    protected Object parse(String argument) throws CommandArgumentException {
        for (String enumName : enumMap.keySet()) {
            if (enumName.equalsIgnoreCase(argument)) {
                return enumMap.get(enumName);
            }
        }

        throw new CommandArgumentException(argument + " is not a valid option.");
    }

    @Override
    public List<String> getSuggestions(CommandContext context) {
        return new ArrayList<>(enumMap.keySet());
    }
}

package me.rojetto.comfy;

import me.rojetto.comfy.argument.*;
import me.rojetto.comfy.tree.Argument;
import me.rojetto.comfy.tree.Literal;

public class CommandTreeUtil {
    public static Literal literal(String name, String... aliases) {
        return new Literal(name, aliases);
    }

    public static Argument argument(String name, ArgumentType type) {
        return new Argument(name, type);
    }

    public static StringType stringType() {
        return new StringType();
    }

    public static IntegerType intType() {
        return new IntegerType();
    }

    public static <T extends Enum> EnumType<T> enumType(T[] enumValues, String[] names) {
        return new EnumType<>(enumValues, names);
    }

    public static BooleanType booleanType() {
        return new BooleanType();
    }

    public static DoubleType doubleType() {
        return new DoubleType();
    }

    public static FloatType floatType() {
        return new FloatType();
    }

    public static Flag flag(String label, ArgumentType type) {
        return new Flag(label, type);
    }

    public static Flag flag(String label) {
        return flag(label, null);
    }
}

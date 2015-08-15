package me.rojetto.comfy;

public class Flag {
    private final String label;
    private final ArgumentType type;

    public Flag(String label, ArgumentType type) {
        this.label = label;
        this.type = type;
    }

    public Flag(String label) {
        this(label, null);
    }

    public String getLabel() {
        return label;
    }

    public ArgumentType getType() {
        return type;
    }
}

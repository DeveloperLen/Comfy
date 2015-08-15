package me.rojetto.comfy.exception;

public class ArgumentException extends Exception {
    private final String argumentName;

    public ArgumentException(String argumentName, String msg) {
        super(msg);
        this.argumentName = argumentName;
    }

    public String getArgumentName() {
        return argumentName;
    }
}

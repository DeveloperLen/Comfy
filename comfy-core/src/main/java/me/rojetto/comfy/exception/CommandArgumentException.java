package me.rojetto.comfy.exception;

public class CommandArgumentException extends Exception {
    private final String argumentName;

    public CommandArgumentException(String argumentName, String msg) {
        super(msg);
        this.argumentName = argumentName;
    }

    public String getArgumentName() {
        return argumentName;
    }
}

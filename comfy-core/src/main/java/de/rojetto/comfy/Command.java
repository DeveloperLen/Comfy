package de.rojetto.comfy;

public class Command {
    private final CommandSender sender;
    private final String path;
    private final Arguments arguments;

    protected Command(CommandSender sender, String path, Arguments arguments) {
        this.sender = sender;
        this.path = path;
        this.arguments = arguments;
    }

    public CommandSender getSender() {
        return sender;
    }

    public String getPath() {
        return path;
    }

    public Arguments getArguments() {
        return arguments;
    }
}

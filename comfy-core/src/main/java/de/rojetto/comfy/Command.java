package de.rojetto.comfy;

public class Command {
    private final CommandSender sender;
    private final Arguments arguments;

    protected Command(CommandSender sender, Arguments arguments) {
        this.sender = sender;
        this.arguments = arguments;
    }

    public CommandSender getSender() {
        return sender;
    }

    public Arguments getArguments() {
        return arguments;
    }
}

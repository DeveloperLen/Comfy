package me.rojetto.comfy;

import me.rojetto.comfy.tree.CommandPath;

public abstract class CommandContext {
    private final CommandSender sender;
    private final CommandPath path;
    private final Arguments arguments;

    protected CommandContext(CommandSender sender, CommandPath path, Arguments arguments) {
        this.sender = sender;
        this.path = path;
        this.arguments = arguments;
    }

    public CommandSender getSender() {
        return sender;
    }

    public CommandPath getPath() {
        return path;
    }

    public Arguments getArguments() {
        return arguments;
    }
}

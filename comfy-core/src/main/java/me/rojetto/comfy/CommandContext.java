package me.rojetto.comfy;

import me.rojetto.comfy.tree.CommandPath;

public abstract class CommandContext<S extends CommandSender> {
    private final S sender;
    private final CommandPath path;
    private final Arguments arguments;

    protected CommandContext(S sender, CommandPath path, Arguments arguments) {
        this.sender = sender;
        this.path = path;
        this.arguments = arguments;
    }

    public S getSender() {
        return sender;
    }

    public CommandPath getPath() {
        return path;
    }

    public Arguments getArguments() {
        return arguments;
    }
}

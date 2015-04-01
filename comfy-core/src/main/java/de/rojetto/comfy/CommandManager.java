package de.rojetto.comfy;

import de.rojetto.comfy.exception.CommandArgumentException;
import de.rojetto.comfy.exception.CommandPathException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class CommandManager {
    private final CommandRoot root;
    private final List<CommandListener> listeners;

    public CommandManager() {
        this.root = new CommandRoot();

        this.listeners = new ArrayList<>();
    }

    public void addCommand(CommandNode commandNode) {
        root.child(commandNode);
    }

    protected void process(CommandSender sender, String commandString) {
        List<String> segments = Arrays.asList(commandString.split(" "));

        try {
            CommandPath path = root.parsePath(segments, false);
            Arguments args = path.parseArguments(segments);

            CommandContext context = buildContext(sender, path, args);

            //TODO: Actually call the handler method
        } catch (CommandPathException e) {
            sender.warning(e.getMessage()); //TODO: Help with usage (paths to next executes or something)
        } catch (CommandArgumentException e) {
            sender.warning(e.getMessage());
        }
    }

    public void addListener(CommandListener listener) {
        listeners.add(listener);
    }

    protected abstract CommandContext buildContext(CommandSender sender, CommandPath path, Arguments arguments);

    public abstract void registerCommands();
}

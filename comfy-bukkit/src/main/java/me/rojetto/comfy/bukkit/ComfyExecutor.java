package me.rojetto.comfy.bukkit;

import me.rojetto.comfy.CommandManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ComfyExecutor implements CommandExecutor {
    private final CommandManager manager;

    public ComfyExecutor(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String line = label;
        for (String arg : args) {
            line += " " + arg;
        }
        manager.process(new BukkitCommandSender(sender), line);
        return true;
    }
}

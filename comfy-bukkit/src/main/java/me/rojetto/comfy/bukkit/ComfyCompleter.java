package me.rojetto.comfy.bukkit;

import me.rojetto.comfy.CommandManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ComfyCompleter implements TabCompleter {
    private final CommandManager manager;

    public ComfyCompleter(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> segments = new ArrayList<>();
        segments.add(alias);
        segments.addAll(Arrays.asList(args));

        return manager.tabComplete(new BukkitCommandSender(sender), segments);
    }
}

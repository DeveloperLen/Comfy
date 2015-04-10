package me.rojetto.comfy.bukkit;

import me.rojetto.comfy.CommandSender;
import me.rojetto.comfy.tree.CommandNode;
import me.rojetto.comfy.tree.CommandPath;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class BukkitCommandSender implements CommandSender {
    private final org.bukkit.command.CommandSender sender;

    protected BukkitCommandSender(org.bukkit.command.CommandSender sender) {
        this.sender = sender;
    }

    public boolean isPlayer() {
        return sender instanceof Player;
    }

    public org.bukkit.command.CommandSender getSender() {
        return sender;
    }

    @Override
    public void warning(String message) {
        sender.sendMessage((isPlayer() ? ChatColor.RED.toString() : "Warning: ") + message);
    }

    @Override
    public void info(String message) {
        sender.sendMessage((isPlayer() ? ChatColor.GRAY.toString() : "Info: ") + message);
    }

    @Override
    public void pathHelp(CommandPath path) {
        CommandNode lastNode = path.getLastNode();
        if (lastNode == null) {
            return;
        }

        String line = (isPlayer() ? ChatColor.GOLD.toString() + "/" : "Path: ") + path;
        if (!lastNode.isExecutable()) {
            line += " ...";
        }
        if (lastNode.hasDescription()) {
            line += (isPlayer() ? ": " + ChatColor.WHITE : " - ") + lastNode.getDescription();
        }

        sender.sendMessage(line);
    }

    @Override
    public boolean hasPermission(String permission) {
        return sender.hasPermission(permission);
    }
}

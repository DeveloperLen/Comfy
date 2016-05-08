package me.rojetto.comfy.bungeecord;

import me.rojetto.comfy.CommandSender;
import me.rojetto.comfy.tree.CommandNode;
import me.rojetto.comfy.tree.CommandPath;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * Created by Tarik on 08.05.2016.
 */
public class BungeeCommandSender implements CommandSender {
    private final net.md_5.bungee.api.CommandSender sender;

    protected BungeeCommandSender(net.md_5.bungee.api.CommandSender sender) {
        this.sender = sender;
    }

    public boolean isPlayer() {
        return sender instanceof ProxiedPlayer;
    }

    public net.md_5.bungee.api.CommandSender getSender() {
        return sender;
    }

    @Override
    public void warning(String message) {
        sender.sendMessage(new TextComponent((isPlayer() ? ChatColor.RED.toString() : "Warning: ") + message));
    }

    @Override
    public void info(String message) {
        sender.sendMessage(new TextComponent((isPlayer() ? ChatColor.GRAY.toString() : "Info: ") + message));
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

        sender.sendMessage(new TextComponent(line));
    }

    @Override
    public boolean hasPermission(String permission) {
        return sender.hasPermission(permission);
    }
}

package me.rojetto.comfy.bukkit;

import me.rojetto.comfy.Arguments;
import me.rojetto.comfy.CommandContext;
import me.rojetto.comfy.tree.CommandPath;

public class BukkitCommandContext extends CommandContext<BukkitCommandSender> {
    protected BukkitCommandContext(BukkitCommandSender sender, CommandPath path, Arguments arguments) {
        super(sender, path, arguments);
    }
}

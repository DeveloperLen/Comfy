package me.rojetto.comfy.bungeecord;

import me.rojetto.comfy.Arguments;
import me.rojetto.comfy.CommandContext;
import me.rojetto.comfy.tree.CommandPath;

/**
 * Created by Tarik on 08.05.2016.
 */
public class BungeeCommandContext extends CommandContext<BungeeCommandSender> {
    protected BungeeCommandContext(BungeeCommandSender sender, CommandPath path, Arguments arguments) {
        super(sender, path, arguments);
    }
}

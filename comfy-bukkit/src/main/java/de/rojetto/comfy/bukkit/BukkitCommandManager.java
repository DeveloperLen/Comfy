package de.rojetto.comfy.bukkit;

import de.rojetto.comfy.*;

public class BukkitCommandManager extends CommandManager {
    @Override
    protected CommandContext buildContext(CommandSender sender, CommandPath path, Arguments arguments) {
        return null;
    }

    @Override
    public void registerCommands() {

    }
}

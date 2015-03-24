package de.rojetto.comfy.bukkit.argument;

import de.rojetto.comfy.CommandArgument;
import de.rojetto.comfy.exception.CommandArgumentException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerArgument extends CommandArgument {
    public PlayerArgument(String name) {
        super(name);
    }

    @Override
    protected Object parse(String argument) throws CommandArgumentException {
        Player player = null;

        for (Player onlinePlayer : Bukkit.getOnlinePlayers())
            if (onlinePlayer.getName().equalsIgnoreCase(argument))
                player = onlinePlayer;

        if (player == null)
            throw new CommandArgumentException("Player " + argument + " doesn't exist");

        return player;
    }
}

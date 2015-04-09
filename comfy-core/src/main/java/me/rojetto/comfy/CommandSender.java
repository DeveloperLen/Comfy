package me.rojetto.comfy;

import me.rojetto.comfy.tree.CommandPath;

public interface CommandSender {
    void warning(String message);

    void info(String message);

    void pathHelp(CommandPath path);

    boolean hasPermission(String permission);
}

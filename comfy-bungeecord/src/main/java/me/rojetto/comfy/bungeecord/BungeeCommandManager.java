package me.rojetto.comfy.bungeecord;

import me.rojetto.comfy.Arguments;
import me.rojetto.comfy.CommandManager;
import me.rojetto.comfy.tree.CommandNode;
import me.rojetto.comfy.tree.CommandPath;
import me.rojetto.comfy.tree.Literal;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

/**
 * Created by Tarik on 08.05.2016.
 */
public class BungeeCommandManager extends CommandManager<BungeeCommandContext, BungeeCommandSender> {
    private final Plugin plugin;

    public BungeeCommandManager(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected BungeeCommandContext buildContext(BungeeCommandSender sender, CommandPath path, Arguments arguments) {
        return new BungeeCommandContext(sender, path, arguments);
    }

    @Override
    protected void onRegisterCommands() {
        PluginManager pluginManager = plugin.getProxy().getPluginManager();
        for (CommandNode node : getRoot().getChildren()) {
            if (!(node instanceof Literal)) {
                continue;
            }

            final Literal literal = (Literal) node;

            pluginManager.registerCommand(plugin, new Command(literal.getLabel(), (!literal.hasPermission() ? null : literal.getPermission()), literal.getAliases().toArray(new String[0])) {
                @Override
                public void execute(CommandSender sender, String[] args) {
                    String line = literal.getLabel();
                    for (String arg : args) {
                        line += " " + arg;
                    }
                    process(new BungeeCommandSender(sender), line);
                }
            });
        }
    }
}

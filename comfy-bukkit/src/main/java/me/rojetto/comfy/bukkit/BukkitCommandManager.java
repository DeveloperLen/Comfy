package me.rojetto.comfy.bukkit;

import me.rojetto.comfy.Arguments;
import me.rojetto.comfy.CommandManager;
import me.rojetto.comfy.tree.CommandNode;
import me.rojetto.comfy.tree.CommandPath;
import me.rojetto.comfy.tree.Literal;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class BukkitCommandManager extends CommandManager<BukkitCommandContext, BukkitCommandSender> {
    private final JavaPlugin plugin;

    public BukkitCommandManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected BukkitCommandContext buildContext(BukkitCommandSender sender, CommandPath path, Arguments arguments) {
        return new BukkitCommandContext(sender, path, arguments);
    }

    @Override
    protected void onRegisterCommands() {
        CommandExecutor executor = new ComfyExecutor(this);
        TabCompleter completer = new ComfyCompleter(this);

        try {
            CommandMap map = getBukkitCommandMap();

            for (CommandNode node : getRoot().getChildren()) {
                if (!(node instanceof Literal)) {
                    continue;
                }

                Literal literal = (Literal) node;

                PluginCommand pluginCommand = newCommand(literal.getLabel());
                pluginCommand.setExecutor(executor);
                pluginCommand.setTabCompleter(completer);
                pluginCommand.setAliases(literal.getAliases());
                pluginCommand.setUsage("/" + literal.getLabel() + " ?");
                if (literal.hasDescription()) {
                    pluginCommand.setDescription(literal.getDescription());
                }
                if (literal.hasPermission()) {
                    pluginCommand.setPermission(literal.getPermission());
                }

                map.register(plugin.getName().toLowerCase(), pluginCommand); // TODO: Is this the right prefix?
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private CommandMap getBukkitCommandMap() throws IllegalAccessException, NoSuchFieldException {
        Field bukkitCommandMap = plugin.getServer().getClass().getDeclaredField("commandMap");

        bukkitCommandMap.setAccessible(true);
        CommandMap commandMap = (CommandMap) bukkitCommandMap.get(plugin.getServer());

        return commandMap;
    }

    private PluginCommand newCommand(String label) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
        constructor.setAccessible(true);

        PluginCommand command = constructor.newInstance(label, plugin);

        return command;
    }
}

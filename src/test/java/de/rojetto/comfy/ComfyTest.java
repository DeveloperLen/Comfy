package de.rojetto.comfy;

import de.rojetto.comfy.argument.StringArgument;
import org.bukkit.command.CommandSender;
import org.junit.Test;

public class ComfyTest implements CommandListener {
    @Test
    public void test() {
        CommandManager manager = new CommandManager();
        CommandNode cmd1 = manager.commandRoot().child("cmd1");
        cmd1.child("cmd11").required(new StringArgument("arg111"));
        cmd1.child("cmd12").required(new StringArgument("arg121")).required(new StringArgument("arg122"));

        CommandNode cmd2 = manager.commandRoot().child("cmd2").required(new StringArgument("arg21"));
        cmd2.child("cmd21").optional(new StringArgument("opt1")).optional(new StringArgument("opt2"));
        cmd2.child("cmd22");

        CommandNode cmd3 = manager.commandRoot().child("cmd3");
        cmd3.child("cmd31").required(new StringArgument("arg311"));
        cmd3.child("cmd32").required(new StringArgument("arg321")).required(new StringArgument("arg322"));

        manager.call("cmd1 cmd31 aaa");
    }

    @CommandHandler("")
    public void addObjective(Command command) {
        CommandSender sender = command.getSender();
        String name = command.getArguments().getString("name");
        String criteria = command.getArguments().getString("criteria");
    }
}

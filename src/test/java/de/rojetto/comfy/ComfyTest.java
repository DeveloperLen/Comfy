package de.rojetto.comfy;

import de.rojetto.comfy.argument.IntegerArgument;
import de.rojetto.comfy.argument.StringArgument;
import org.junit.Test;

public class ComfyTest implements CommandListener {
    @Test
    public void test() {
        CommandManager manager = new CommandManager();
        manager.addListener(this);
        CommandNode cmd1 = manager.commandRoot().child("cmd1");
        cmd1.child("cmd11").required(new StringArgument("arg111"));
        cmd1.child("cmd12").required(new StringArgument("arg121")).required(new StringArgument("arg122"));

        CommandNode cmd2 = manager.commandRoot().child("cmd2").required(new StringArgument("arg21"));
        cmd2.child("cmd21").required(new IntegerArgument("arg211")).required(new StringArgument("arg212"));
        cmd2.child("cmd22");

        CommandNode cmd3 = manager.commandRoot().child("cmd3");
        cmd3.child("cmd31").required(new StringArgument("arg311"));
        cmd3.child("cmd32").required(new StringArgument("arg321")).required(new StringArgument("arg322"));

        manager.call(new ConsoleSender(), "cmd2 aaa cmd21 1 ccc ddd");
    }
}

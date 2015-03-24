package de.rojetto.comfy;

import de.rojetto.comfy.argument.IntegerArgument;
import de.rojetto.comfy.argument.StringArgument;
import org.junit.Test;

public class ComfyTest implements CommandListener {
    @Test
    public void test() {
        TestCommandManager manager = new TestCommandManager();
        manager.addListener(this);
        CommandNode cmd1 = manager.commands().child("cmd1");
        cmd1.child("cmd11").required(new StringArgument("arg111"));
        cmd1.child("cmd12").required(new StringArgument("arg121")).required(new StringArgument("arg122"));

        CommandNode cmd2 = manager.commands().child("cmd2").required(new StringArgument("arg21"));
        cmd2.child("cmd21").required(new IntegerArgument("arg211")).required(new StringArgument("arg212"));
        cmd2.child("cmd22");

        CommandNode cmd3 = manager.commands().child("cmd3");
        cmd3.child("cmd31").required(new StringArgument("arg311"));
        cmd3.child("cmd32").required(new StringArgument("arg321")).required(new StringArgument("arg322"));


        manager.registerCommands();
        manager.callTestCommand(new ConsoleSender(), "cmd1 aaa cmd21 1 ccc ddd");
    }

    @CommandHandler("cmd1.cmd12")
    public void testCommandHandler(Command command) {
        command.getSender().info(command.getPath());
    }

    class TestCommandManager extends CommandManager {
        @Override
        public void registerCommands() {

        }

        public void callTestCommand(CommandSender sender, String commandString) {
            this.process(sender, commandString);
        }
    }
}

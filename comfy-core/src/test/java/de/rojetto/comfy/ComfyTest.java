package de.rojetto.comfy;

import de.rojetto.comfy.argument.IntegerArgument;
import de.rojetto.comfy.argument.StringArgument;
import org.junit.Test;

public class ComfyTest implements CommandListener {
    @Test
    public void test() {
        TestCommandManager manager = new TestCommandManager();
        manager.addListener(this);
        manager.registerCommand(new Literal("command")
                        .child(new Literal("list"))
                        .child(new StringArgument("name")
                                .child(new Literal("add"))
                                .child(new Literal("remove")))
        );

        manager.registerCommand(new Literal("command2")
                .child(new IntegerArgument("number"))
                .child(new StringArgument("string")));

        manager.callTestCommand(new ConsoleSender(), "command heyhey add");
    }

    public void testCommand(int arg1, boolean arg2, int arg3) {

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

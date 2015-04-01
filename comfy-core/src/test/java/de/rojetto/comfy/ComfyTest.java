package de.rojetto.comfy;

import de.rojetto.comfy.argument.IntegerArgument;
import de.rojetto.comfy.argument.StringArgument;
import org.junit.Test;

public class ComfyTest implements CommandListener {
    @Test
    public void test() {
        TestCommandManager manager = new TestCommandManager();
        manager.addListener(this);
        manager.addCommand(new Literal("command")
                        .child(new Literal("list").executes("listCommands"))
                        .child(new StringArgument("name")
                                .child(new Literal("add"))
                                .child(new Literal("remove")))
        );

        manager.addCommand(new Literal("command2")
                .child(new IntegerArgument("number"))
                .child(new StringArgument("string")));

        manager.callTestCommand(new ConsoleSender(), "command list");
    }

    @CommandHandler("listCommands")
    public void listCommands(TestCommandContext context) {

    }


    class TestCommandManager extends CommandManager {
        @Override
        protected CommandContext buildContext(CommandSender sender, CommandPath path, Arguments arguments) {
            return new TestCommandContext(sender, path, arguments);
        }

        @Override
        public void registerCommands() {

        }

        public void callTestCommand(CommandSender sender, String commandString) {
            this.process(sender, commandString);
        }
    }

    class TestCommandContext extends CommandContext {
        protected TestCommandContext(CommandSender sender, CommandPath path, Arguments arguments) {
            super(sender, path, arguments);
        }
    }
}

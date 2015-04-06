package me.rojetto.comfy;

import me.rojetto.comfy.argument.StringArgument;
import org.junit.Test;

public class ComfyTest implements CommandListener {
    @Test
    public void test() {
        TestCommandManager manager = new TestCommandManager();
        manager.addListener(this);
        manager.addCommand(new Literal("command", "c")
                        .child(new Literal("list").executes("listCommands"))
                        .child(new Literal("message", "msg")
                                .child(new StringArgument("text").executes("msgCommand")))
                        .child(new Literal("opt")
                                .child(new StringArgument("r1").executes("optCommand")
                                        .child(new StringArgument("o1")
                                                .child(new StringArgument("o2")))))
                        .child(new StringArgument("name")
                                .child(new Literal("add").executes("addCommand"))
                                .child(new Literal("remove").executes("removeCommand")))
        );

        manager.registerCommands();
        manager.callTestCommand(new ConsoleSender(), "command opt 1 2 3 4");
    }

    @CommandHandler("listCommands")
    public void testCommandHandler(TestCommandContext context) {
        context.getSender().info(context.getPath() + "; " + context.getArguments());
    }

    @CommandHandler("addCommand")
    public void addCommand(TestCommandContext context) {
        context.getSender().info(context.getPath() + "; " + context.getArguments());
    }

    @CommandHandler("removeCommand")
    public void removeCommand(TestCommandContext context) {
        context.getSender().info(context.getPath() + "; " + context.getArguments());
    }

    @CommandHandler("msgCommand")
    public void msgCommand(TestCommandContext context) {
        context.getSender().info(context.getPath() + "; " + context.getArguments());
    }

    @CommandHandler("optCommand")
    public void optCommand(TestCommandContext context, @Arg("o2") String second_argument) {
        context.getSender().info(context.getPath() + "; " + context.getArguments());
        System.out.println(second_argument);
    }

    class TestCommandManager extends CommandManager {
        @Override
        protected CommandContext buildContext(CommandSender sender, CommandPath path, Arguments arguments) {
            return new TestCommandContext(sender, path, arguments);
        }

        @Override
        protected void onRegisterCommands() {

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

    class ConsoleSender implements CommandSender {
        @Override
        public void warning(String message) {
            System.out.println("[warning] " + message);
        }

        @Override
        public void info(String message) {
            System.out.println("[info] " + message);
        }
    }
}

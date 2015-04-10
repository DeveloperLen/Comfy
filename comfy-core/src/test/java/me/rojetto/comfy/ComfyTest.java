package me.rojetto.comfy;

import me.rojetto.comfy.annotation.CommandHandler;
import me.rojetto.comfy.argument.BooleanArgument;
import me.rojetto.comfy.argument.EnumArgument;
import me.rojetto.comfy.argument.IntegerArgument;
import me.rojetto.comfy.argument.StringArgument;
import me.rojetto.comfy.tree.CommandNode;
import me.rojetto.comfy.tree.CommandPath;
import me.rojetto.comfy.tree.Literal;
import org.junit.Test;

import java.util.List;

public class ComfyTest implements CommandListener {
    @Test
    public void test() {
        TestCommandManager manager = new TestCommandManager();
        manager.addListener(this);
        manager.addCommand(new Literal("command", "c").description("Complicated test commands").executes("command").permission("yes")
                        .child(new Literal("list").executes("listCommands"))
                        .child(new Literal("message", "msg")
                                .child(new StringArgument("text").executes("msgCommand")))
                        .child(new Literal("opt")
                                .child(new StringArgument("r1").executes("optCommand")
                                        .child(new IntegerArgument("o1")
                                                .child(new StringArgument("o2")))))
                        .child(new Literal("bool")
                                .child(new BooleanArgument("boolean").executes("bool")))
                        .child(new Literal("enum")
                                .child(new EnumArgument("enumeration", TestEnum.values(), new String[]{"one", "two", "three"}).executes("enum").description("Enum command")))
                        .child(new Literal("multi").executes("multi").description("Is not the last in this path")
                                .child(new Literal("exe").executes("exe")))
                        .child(new StringArgument("name").description("All name commands")
                                .child(new Literal("add").executes("addCommand"))
                                .child(new Literal("remove").executes("removeCommand")))
        );

        manager.addCommand(new Literal("desc").description("All desc commands")
                .child(new Literal("sub1").description("Sub category 1")
                        .child(new Literal("ex1").executes("ex1").description("Executes 1"))
                        .child(new Literal("ex2").executes("ex2").description("Executes 2")))
                .child(new Literal("ex3").executes("ex3").description("Executes 3")));

        manager.registerCommands();
        manager.callTestCommand("command list hey");
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
    public void optCommand(TestCommandContext context) {
        context.getSender().info(context.getPath() + "; " + context.getArguments());
    }

    @CommandHandler("bool")
    public void bool(TestCommandContext context) {
        context.getSender().info(context.getPath() + "; " + context.getArguments());
    }

    @CommandHandler("enum")
    public void enumCommand(TestCommandContext context) {
        context.getSender().info(context.getPath() + "; " + context.getArguments());
    }

    class TestCommandManager extends CommandManager<TestCommandContext, ConsoleSender> {
        @Override
        protected TestCommandContext buildContext(ConsoleSender sender, CommandPath path, Arguments arguments) {
            return new TestCommandContext(sender, path, arguments);
        }

        @Override
        protected void onRegisterCommands() {

        }

        public void callTestCommand(String commandString) {
            this.process(new ConsoleSender(), commandString);
        }

        public void tabCompleteTest(String commandString) {
            List<String> suggestions = null;
            suggestions = this.tabComplete(new ConsoleSender(), split(commandString));

            for (String suggestion : suggestions) {
                System.out.print(suggestion + " ");
            }
        }
    }

    class TestCommandContext extends CommandContext<ConsoleSender> {
        protected TestCommandContext(ConsoleSender sender, CommandPath path, Arguments arguments) {
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

        @Override
        public void pathHelp(CommandPath path) {
            CommandNode lastNode = path.getLastNode();
            if (lastNode == null) {
                return;
            }

            String line = "[path] " + path;
            if (!lastNode.isExecutable()) {
                line += " ...";
            }
            if (lastNode.hasDescription()) {
                line += " - " + lastNode.getDescription();
            }

            System.out.println(line);
        }

        @Override
        public boolean hasPermission(String permission) {
            return permission.equals("yes");
        }
    }

    enum TestEnum {
        OPTION_ONE, OPTION_TWO, OPTION_THREE;
    }
}

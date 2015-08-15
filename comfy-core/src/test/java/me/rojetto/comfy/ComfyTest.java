package me.rojetto.comfy;

import me.rojetto.comfy.annotation.Arg;
import me.rojetto.comfy.annotation.CommandHandler;
import me.rojetto.comfy.tree.CommandNode;
import me.rojetto.comfy.tree.CommandPath;
import org.junit.Test;

import java.util.List;

import static me.rojetto.comfy.CommandTreeUtil.*;

public class ComfyTest implements CommandListener {
    @Test
    public void test() {
        TestCommandManager manager = new TestCommandManager();
        manager.addListener(this);
        manager.addCommand(literal("command", "c").description("Complicated test commands").executes("command").permission("yes")
                        .then(literal("list").executes("listCommands"))
                        .then(literal("message", "msg")
                                .then(argument("text", stringType()).executes("msgCommand")))
                        .then(literal("opt")
                                .then(argument("r1", stringType()).executes("optCommand")
                                        .then(argument("o1", intType())
                                                .then(argument("o2", stringType())))))
                        .then(literal("bool")
                                .then(argument("boolean", booleanType()).executes("bool")))
                        .then(literal("enum")
                                .then(argument("enumeration", enumType(TestEnum.values(), new String[]{"one", "two", "three"})).executes("enum").description("Enum command")))
                        .then(literal("multi").executes("multi").description("Is not the last in this path")
                                .then(literal("exe").executes("exe")))
                        .then(argument("name", stringType()).description("All name commands")
                                .then(literal("add").executes("addCommand"))
                                .then(literal("remove").executes("removeCommand")))
        );
        manager.addCommand(literal("desc").description("All desc commands")
                .then(literal("sub1").description("Sub category 1")
                        .then(literal("ex1").executes("ex1").description("Executes 1"))
                        .then(literal("ex2").executes("ex2").description("Executes 2")))
                .then(literal("ex3").executes("ex3").description("Executes 3")));
        manager.registerCommands();

        manager.callTestCommand("c enum one");
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
    public void enumCommand(TestCommandContext context, @Arg("enumeration") TestEnum meinEnumArgument) {
        context.getSender().info(context.getPath() + "; " + context.getArguments());
        context.getSender().info(meinEnumArgument.toString());
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

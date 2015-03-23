package de.rojetto.comfy;

public class ConsoleSender implements CommandSender {
    @Override
    public void warning(String message) {
        System.out.println("[warning] " + message);
    }

    @Override
    public void info(String message) {
        System.out.println("[info] " + message);
    }
}

package me.rojetto.comfy;

public class CommandRoot extends CommandNode {
    @Override
    public boolean matches(String segmentString) {
        return false;
    }

    @Override
    public boolean isExecutable() {
        return false;
    }
}

package de.rojetto.comfy;

public class CommandRoot extends CommandNode {
    @Override
    public boolean matches(String segmentString) {
        return false;
    }

    @Override
    public String getExecutor() {
        return null;
    }
}
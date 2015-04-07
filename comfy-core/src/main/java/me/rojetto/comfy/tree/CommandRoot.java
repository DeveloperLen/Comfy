package me.rojetto.comfy.tree;

import me.rojetto.comfy.CommandContext;

import java.util.List;

public class CommandRoot extends CommandNode {
    @Override
    public boolean matches(String segmentString) {
        return false;
    }

    @Override
    public List<String> getSuggestions(CommandContext context) {
        return null;
    }

    @Override
    public boolean isExecutable() {
        return false;
    }
}

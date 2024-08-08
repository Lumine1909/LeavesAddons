package io.github.lumine1909.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CommandOverrideHandler {

    private static final Map<String, Set<CommandOverrider>> overriders = new HashMap<>();

    public void register(String command, CommandOverrider overrider) {
        if (!overriders.containsKey(command)) {
            overriders.put(command, new HashSet<>());
        }
        overriders.get(command).add(overrider);
    }

    public void callTabComplete(String command, CommandSender sender, String[] args) {
        if (overriders.containsKey(command)) {
            for (CommandOverrider overrider : overriders.get(command)) {
                overrider.onTabComplete(command, sender, args);
            }
        }
    }
}

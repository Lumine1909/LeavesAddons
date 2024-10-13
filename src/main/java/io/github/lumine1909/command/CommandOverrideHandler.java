package io.github.lumine1909.command;

import org.bukkit.command.CommandSender;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.TabCompleteEvent;

import java.util.*;

public class CommandOverrideHandler {

    private static final Map<String, CommandOverrider> overriders = new HashMap<>();

    public void register(String command, CommandOverrider overrider, boolean forceReplace) {
        if (forceReplace || !overriders.containsKey(command)) {
            overriders.put(command, overrider);
        }
    }

    public void callTabComplete(TabCompleteEvent e) {
        String command = e.getBuffer().split(" ")[0];
        CommandSender sender = e.getSender();
        String[] args = e.getBuffer().substring(e.getBuffer().indexOf(" ") + 1).split(" ");
        if (overriders.containsKey(command)) {
            e.setCompletions(overriders.get(command).onTabComplete(command, sender, args));
        }
    }

    public void callCommand(PlayerCommandPreprocessEvent e) {
        String commandMsg = e.getMessage().substring(1);
        String command = commandMsg.split(" ")[0];
        CommandSender sender = e.getPlayer();
        String[] args = commandMsg.substring(commandMsg.indexOf(" ") + 1).split(" ");
        if (overriders.containsKey(command)) {
            CommandOverrider overrider = overriders.get(command);
            overrider.onCommand(command, sender, args);
            e.setCancelled(true);
        }
    }


}

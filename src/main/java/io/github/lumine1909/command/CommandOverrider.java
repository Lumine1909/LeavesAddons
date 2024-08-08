package io.github.lumine1909.command;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface CommandOverrider {

    List<String> onTabComplete(String command, CommandSender sender, String[] args);
    boolean onCommand(String command, CommandSender sender, String[] args);
}

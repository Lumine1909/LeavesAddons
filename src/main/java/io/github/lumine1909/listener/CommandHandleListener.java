package io.github.lumine1909.listener;

import io.github.lumine1909.command.CommandOverrideHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.server.TabCompleteEvent;

import static io.github.lumine1909.LeavesAddons.plugin;

public class CommandHandleListener implements Listener {

    private final CommandOverrideHandler overrideHandler;

    public CommandHandleListener(CommandOverrideHandler overrideHandler) {
        this.overrideHandler = overrideHandler;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent e) {
        overrideHandler.callCommand(e);
    }

    @EventHandler
    public void onCommandSend(PlayerCommandSendEvent e) {
    }

    @EventHandler
    public void onTabCompleteSend(TabCompleteEvent e) {
        overrideHandler.callTabComplete(e);
    }
}

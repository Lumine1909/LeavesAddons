package io.github.lumine1909.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.server.TabCompleteEvent;

import java.util.ArrayList;
import java.util.List;

public class CommandHandleListener implements Listener {

    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent e) {

    }

    @EventHandler
    public void onCommandSend(PlayerCommandSendEvent e) {

    }

    @EventHandler
    public void onTabCompleteSend(TabCompleteEvent e) {
        List<String> completions = new ArrayList<>(e.getCompletions());
        e.getCompletions().clear();
        //e.getCompletions().addAll(commandOverrideHandler.callTabComplete());
    }
}

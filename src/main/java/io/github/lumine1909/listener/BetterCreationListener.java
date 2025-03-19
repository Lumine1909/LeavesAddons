package io.github.lumine1909.listener;

import io.github.lumine1909.command.BotCreateCommand;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.leavesmc.leaves.event.bot.BotCreateEvent;
import org.leavesmc.leaves.event.bot.BotJoinEvent;
import org.leavesmc.leaves.event.bot.BotRemoveEvent;

import java.util.UUID;

import static io.github.lumine1909.LeavesAddons.plugin;
import static io.github.lumine1909.command.BotCreateCommand.*;

public class BetterCreationListener implements Listener {

    public BetterCreationListener() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBotJoin(BotJoinEvent e) {
        UUID uuid;
        if ((uuid = e.getBot().getCreatePlayerUUID()) != null) {
            creator2BotCountMap.put(uuid, creator2BotCountMap.getOrDefault(uuid, 0) + 1);
        }
    }

    @EventHandler
    public void onBotCreate(BotCreateEvent e) {
        if (e.isCancelled() && pendingBots.contains(e.getBot())) {
            pendingBots.remove(e.getBot());
            e.setCancelled(false);
        }
    }

    @EventHandler
    public void onBotRemove(BotRemoveEvent e) {
        UUID uuid;
        if ((uuid = e.getBot().getCreatePlayerUUID()) != null) {
            creator2BotCountMap.remove(uuid);
        }
    }
}

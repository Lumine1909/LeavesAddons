package io.github.lumine1909.listener;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import static io.github.lumine1909.LeavesAddons.addonsConfig;
import static io.github.lumine1909.LeavesAddons.plugin;

public class WitherRosePlaceListener implements Listener {

    public WitherRosePlaceListener() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onInteract(PlayerInteractEvent e) {
        if (!addonsConfig.BETTER_WITHER_ROSE_PLACE || e.getAction() != Action.RIGHT_CLICK_BLOCK || e.isCancelled() || e.getItem() == null || e.getClickedBlock() == null || e.getBlockFace() != BlockFace.UP || e.getItem().isEmpty() || e.getItem().getType() != Material.WITHER_ROSE) {
            return;
        }
        Location loc = e.getClickedBlock().getLocation().add(0, 1, 0);
        if (loc.getBlock().isReplaceable()) {
            e.getClickedBlock().getLocation().add(0, 1, 0).getBlock().setType(Material.WITHER_ROSE, false);
            e.getItem().setAmount(e.getPlayer().getGameMode() == GameMode.CREATIVE ? e.getItem().getAmount() : e.getItem().getAmount() - 1);
        }
    }
}

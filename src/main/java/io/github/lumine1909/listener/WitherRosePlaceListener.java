package io.github.lumine1909.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import static io.github.lumine1909.LeavesAddons.config;
import static io.github.lumine1909.LeavesAddons.instance;

public class WitherRosePlaceListener implements Listener {

    public WitherRosePlaceListener() {
        Bukkit.getPluginManager().registerEvents(this, instance);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (!config.BETTER_WITHER_ROSE_PLACE || e.getItem() == null || e.getClickedBlock() == null || e.getBlockFace() != BlockFace.UP || e.getItem().isEmpty() || e.getItem().getType() != Material.WITHER_ROSE) {
            return;
        }
        e.getClickedBlock().getLocation().add(0, 1, 0).getBlock().setType(Material.WITHER_ROSE, false);
    }
}

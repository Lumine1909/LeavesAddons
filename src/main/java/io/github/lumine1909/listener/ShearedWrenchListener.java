package io.github.lumine1909.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Crafter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static io.github.lumine1909.LeavesAddons.config;
import static io.github.lumine1909.LeavesAddons.instance;

public class ShearedWrenchListener implements Listener {

    public ShearedWrenchListener() {
        Bukkit.getPluginManager().registerEvents(this, instance);
    }

    private static Map<Class<? extends BlockData>, Function<BlockData, BlockData>> redstoneBlocks = Map.of(
            Directional.class, data -> {
                Directional dir = (Directional) data;
                List<BlockFace> faces = new ArrayList<>(dir.getFaces());
                int face = (faces.indexOf(dir.getFacing()) + 1) % faces.size();
                dir.setFacing(faces.get(face));
                return data;
            },
            Crafter.class, data -> {
                Crafter crafter = (Crafter) data;
                int face = (crafter.getOrientation().ordinal() + 1) % Crafter.Orientation.values().length;
                crafter.setOrientation(Crafter.Orientation.values()[face]);
                return data;
            }
    );

    @EventHandler
    public void onShearedWrench(PlayerInteractEvent e) {
        if (!config.REDSTONE_SHEARS_WRENCH || e.getAction() != Action.RIGHT_CLICK_BLOCK || e.getPlayer().getInventory().getItemInMainHand().isEmpty() || e.getPlayer().getInventory().getItemInMainHand().getType() != Material.SHEARS) {
            return;
        }
        e.setCancelled(true);
        BlockData data = tryRotate(e.getClickedBlock().getBlockData());
        e.getClickedBlock().setBlockData(data);
    }

    private BlockData tryRotate(BlockData data) {
        for (Map.Entry<Class<? extends BlockData>, Function<BlockData, BlockData>> entry : redstoneBlocks.entrySet()) {
            if (entry.getKey().isAssignableFrom(data.getClass())) {
                return entry.getValue().apply(data);
            }
        }
        return data;
    }
}

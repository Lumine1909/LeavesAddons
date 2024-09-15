package io.github.lumine1909.listener;

import com.google.common.collect.ImmutableMap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Rail;
import org.bukkit.block.data.type.Crafter;
import org.bukkit.block.data.type.Piston;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import static io.github.lumine1909.LeavesAddons.config;
import static io.github.lumine1909.LeavesAddons.instance;
import static org.bukkit.Material.*;

public class ShearsWrenchListener implements Listener {

    static {
        ImmutableMap.Builder<Material, Integer> mapBuilder = ImmutableMap.builder();
        for (Material material : Material.values()) {
            if (material.name().endsWith("BUTTON") || material.name().endsWith("DOOR")
                    || material.name().endsWith("GATE") || material.name().endsWith("CHEST")) {
                mapBuilder.put(material, 1);
            }
            if (material.name().endsWith("RAIL")) {
                mapBuilder.put(material, 3);
            }
        }
        mapBuilder.put(REPEATER, 1);
        mapBuilder.put(DISPENSER, 1);
        mapBuilder.put(COMPARATOR, 1);
        mapBuilder.put(DROPPER, 1);
        mapBuilder.put(HOPPER, 1);
        mapBuilder.put(PISTON, 1);
        mapBuilder.put(LEVER, 1);
        mapBuilder.put(OBSERVER, 1);
        mapBuilder.put(LECTERN, 1);
        mapBuilder.put(CALIBRATED_SCULK_SENSOR, 1);;
        mapBuilder.put(TRIPWIRE_HOOK, 1);
        mapBuilder.put(BIG_DRIPLEAF, 1);
        mapBuilder.put(BELL, 1);
        mapBuilder.put(DECORATED_POT, 1);
        mapBuilder.put(CHISELED_BOOKSHELF, 1);
        mapBuilder.put(BARREL, 1);
        mapBuilder.put(FURNACE, 1);
        mapBuilder.put(LIGHTNING_ROD, 1);
        mapBuilder.put(CRAFTER, 2);
        redstoneMap = mapBuilder.build();
    }

    public ShearsWrenchListener() {
        Bukkit.getPluginManager().registerEvents(this, instance);
    }

    private static Map<Material, Integer> redstoneMap;
    private static List<BiFunction<BlockData, Boolean, BlockData>> functions = List.of(
            (data, inverse) -> data,
            (data, inverse) -> {
                Directional dire = (Directional) data;
                List<BlockFace> faces = new ArrayList<>(dire.getFaces());
                int face = Math.floorMod(faces.indexOf(dire.getFacing()) + (inverse ? -1 : 1), faces.size());
                dire.setFacing(faces.get(face));
                return dire;
            },
            (data, inverse) -> {
                Crafter crafter = (Crafter) data;
                int face = Math.floorMod(crafter.getOrientation().ordinal() + (inverse ? -1 : 1), Crafter.Orientation.values().length);
                crafter.setOrientation(Crafter.Orientation.values()[face]);
                return crafter;
            },
            (data, inverse) -> {
                Rail rail = (Rail) data;
                List<Rail.Shape> shapes = new ArrayList<>(rail.getShapes());
                int shape = Math.floorMod(shapes.indexOf(rail.getShape()) + (inverse ? -1 : 1), shapes.size());
                rail.setShape(shapes.get(shape));
                return rail;
            }
    );

    @EventHandler (priority = EventPriority.MONITOR)
    public void onShearedWrench(PlayerInteractEvent e) {
        if (!config.REDSTONE_SHEARS_WRENCH || e.getHand() != EquipmentSlot.HAND || e.isCancelled() || e.getAction() != Action.RIGHT_CLICK_BLOCK || e.getPlayer().getInventory().getItemInMainHand().isEmpty() || e.getPlayer().getInventory().getItemInMainHand().getType() != Material.SHEARS) {
            return;
        }
        if (tryRotate(e)) {
            e.setCancelled(true);
        }
    }

    private boolean tryRotate(PlayerInteractEvent e) {
        BlockData data = e.getClickedBlock().getBlockData();
        boolean inverse = e.getPlayer().isSneaking();
        if (data instanceof Piston piston && piston.isExtended()) {
            return false;
        }
        if (!redstoneMap.containsKey(data.getMaterial())) {
            return false;
        }
        e.getClickedBlock().setBlockData(functions.get(redstoneMap.getOrDefault(data.getMaterial(), 0)).apply(data, inverse), false);
        return true;
    }
}

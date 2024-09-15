package io.github.lumine1909.listener;

import org.bukkit.Bukkit;
import org.bukkit.block.ShulkerBox;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import static io.github.lumine1909.LeavesAddons.config;
import static io.github.lumine1909.LeavesAddons.instance;

public class ShulkerStackListener implements Listener {

    public ShulkerStackListener() {
        Bukkit.getPluginManager().registerEvents(this, instance);
    }

    @EventHandler
    public void onEntityPickUp(EntityPickupItemEvent e) {
        modifyItemStackSize(e.getItem().getItemStack());
    }

    @EventHandler
    public void onPlayerPickUp(PlayerAttemptPickupItemEvent e) {
        modifyItemStackSize(e.getItem().getItemStack());
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        modifyItemStackSize(e.getItemDrop().getItemStack());
    }

    @EventHandler
    public void onCraft(CraftItemEvent e) {
        if (e.getCurrentItem() != null) {
            modifyItemStackSize(e.getCurrentItem());
        }
        modifyItemStackSize(e.getCursor());
    }

    @EventHandler
    public void onInventoryCreative(InventoryCreativeEvent e) {
        if (e.getCurrentItem() != null) {
            modifyItemStackSize(e.getCurrentItem());
        }
        modifyItemStackSize(e.getCursor());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getCurrentItem() != null) {
            modifyItemStackSize(e.getCurrentItem());
        }
        modifyItemStackSize(e.getCursor());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        for (ItemStack itemStack : e.getPlayer().getInventory()) {
            modifyItemStackSize(itemStack);
        }
    }

    private void modifyItemStackSize(ItemStack itemStack) {
        if (itemStack == null ||
        itemStack.isEmpty() ||
        !itemStack.getType().name().endsWith("SHULKER_BOX") ||
        !(itemStack.getItemMeta() instanceof BlockStateMeta stateMeta) ||
        !(stateMeta.getBlockState() instanceof ShulkerBox shulkerBox) ||
        !shulkerBox.getInventory().isEmpty() ||
        (config.SHUKER_BOX_STACK == 1 && !itemStack.getItemMeta().hasMaxStackSize()) ||
        (itemStack.getItemMeta().hasMaxStackSize() && itemStack.getItemMeta().getMaxStackSize() == config.SHUKER_BOX_STACK)) {
            return;
        }
        itemStack.editMeta(meta -> meta.setMaxStackSize(config.SHUKER_BOX_STACK));
    }
}

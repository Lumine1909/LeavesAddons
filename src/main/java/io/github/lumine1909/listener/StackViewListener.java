package io.github.lumine1909.listener;

import org.bukkit.Bukkit;
import org.bukkit.block.ShulkerBox;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.leavesmc.leaves.bytebuf.Bytebuf;
import org.leavesmc.leaves.bytebuf.WrappedBytebuf;
import org.leavesmc.leaves.bytebuf.packet.Packet;
import org.leavesmc.leaves.bytebuf.packet.PacketListener;
import org.leavesmc.leaves.bytebuf.packet.PacketType;

import java.util.ArrayList;
import java.util.List;

import static io.github.lumine1909.LeavesAddons.*;

public class StackViewListener implements Listener {

    public StackViewListener() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (!addonsConfig.CLIENT_STACK_VIEW) {
            return;
        }
        Bukkit.getBytebufManager().registerListener(plugin, new PacketListener() {
            @Override
            public Packet onPacketIn(Player player, Packet packet) {
                return packet;
            }

            @Override
            public Packet onPacketOut(Player player, Packet packet) {
                if (packet.type() == PacketType.ClientboundContainerSetSlot) {
                    Bytebuf buf = packet.bytebuf();
                    byte containerId = buf.readByte();
                    int stateId = buf.readVarInt();
                    int slot = buf.readShort();
                    ItemStack item = editItem(buf.readItemStack());
                    if (item == null) {
                        return packet;
                    }
                    Bytebuf newBuf = Bytebuf.buf(4096);
                    newBuf.writeByte(containerId).writeVarInt(stateId).writeShort(slot).writeItemStack(item);
                    return new Packet(packet.type(), newBuf);
                }
                if (packet.type() == PacketType.ClientboundContainerSetContent) {
                    Bytebuf buf = packet.bytebuf();
                    byte containerId = buf.readByte();
                    int stateId = buf.readVarInt();
                    List<ItemStack> items = new ArrayList<>(readItemList(buf));
                    boolean flag = false;
                    for (int i = 0; i < items.size(); i++) {
                        ItemStack item = editItem(items.get(i));
                        if (item != null) {
                            items.set(i, item);
                            flag = true;
                        }
                    }
                    if (!flag) {
                        return packet;
                    }
                    ItemStack item = buf.readItemStack();
                    Bytebuf newBuf = Bytebuf.buf(16384);
                    newBuf.writeByte(containerId).writeVarInt(stateId);
                    writeItemList(newBuf, items);
                    newBuf.writeItemStack(item);
                    return new Packet(packet.type(), newBuf);
                }
                return packet;
            }
        });
    }

    private ItemStack editItem(ItemStack item) {
        int size = getMaxStackSize(item);
        if (size == -1) {
            return null;
        }
        item.editMeta(meta -> meta.setMaxStackSize(size));
        return item;
    }

    private int getMaxStackSize(ItemStack item) {
        if (!item.getType().name().endsWith("SHULKER_BOX")
        || !(item.getItemMeta() instanceof BlockStateMeta blockStateMeta)
        || !(blockStateMeta.getBlockState() instanceof ShulkerBox shulkerBox)
        || !shulkerBox.getInventory().isEmpty()) {
            return -1;
        }
        return Math.max(leavesConfig.getInt("shulkerBoxStackSize"), item.getMaxStackSize());
    }

    private List<ItemStack> readItemList(Bytebuf buf) {
        List<net.minecraft.world.item.ItemStack> nmsItems = net.minecraft.world.item.ItemStack.OPTIONAL_LIST_STREAM_CODEC.decode(((WrappedBytebuf) buf).getRegistryBuf());
        return nmsItems.stream().map(CraftItemStack::asBukkitCopy).toList();
    }

    private void writeItemList(Bytebuf buf, List<ItemStack> itemStackList) {
        List<net.minecraft.world.item.ItemStack> nmsItems = itemStackList.stream().map(CraftItemStack::asNMSCopy).toList();
        net.minecraft.world.item.ItemStack.OPTIONAL_LIST_STREAM_CODEC.encode(((WrappedBytebuf) buf).getRegistryBuf(), nmsItems);
    }
}
package io.github.lumine1909.util;

import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class SkinUtil {

    public static String[] getSkin(Player player) {
        ServerPlayer sp = ((CraftPlayer) player).getHandle();
        return (String[]) sp.getGameProfile().getProperties().get("textures").stream()
            .map(property -> new String[]{property.value(), property.signature()}).toArray()[0];
    }
}

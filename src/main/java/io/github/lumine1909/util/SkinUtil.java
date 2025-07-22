package io.github.lumine1909.util;

import com.mojang.authlib.properties.Property;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class SkinUtil {

    public static String[] getSkin(Player player) {
        ServerPlayer sp = ((CraftPlayer) player).getHandle();
        return sp.getGameProfile().getProperties().get("textures").stream().map(Property::getValue).toArray(String[]::new);
    }

}

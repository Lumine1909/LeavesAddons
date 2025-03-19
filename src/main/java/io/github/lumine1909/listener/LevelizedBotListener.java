package io.github.lumine1909.listener;

import com.mojang.authlib.GameProfile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.leavesmc.leaves.bot.*;
import org.leavesmc.leaves.event.bot.BotLoadEvent;

import java.lang.reflect.Field;
import java.util.*;

import static io.github.lumine1909.LeavesAddons.addonsConfig;
import static io.github.lumine1909.LeavesAddons.plugin;

public class LevelizedBotListener implements Listener {

    private static final BotDataStorage storage;

    static {
        try {
            Field f_dataStorage = BotList.class.getDeclaredField("dataStorage");
            f_dataStorage.setAccessible(true);
            storage = (BotDataStorage) f_dataStorage.get(BotList.INSTANCE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public final Map<UUID, List<String>> unloadedBots = new HashMap<>();
    public final Map<String, World> bot2Level = new HashMap<>();

    public LevelizedBotListener() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBotLoad(BotLoadEvent event) {
        if (!addonsConfig.LEVELIZED_BOT_LOADING) {
            return;
        }
        UUID levelUUID = getBotLevel(event.getBot());
        if (levelUUID != null && Bukkit.getWorld(levelUUID) == null) {
            unloadedBots.computeIfAbsent(levelUUID, v -> new ArrayList<>()).add(event.getBot());
            event.setCancelled(true);
        } else if (levelUUID != null) {
            bot2Level.put(event.getBot(), Bukkit.getWorld(levelUUID));
        }
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        if (!addonsConfig.LEVELIZED_BOT_LOADING) {
            return;
        }
        if (unloadedBots.containsKey(event.getWorld().getUID())) {
            for (String botName : unloadedBots.get(event.getWorld().getUID())) {
                BotList.INSTANCE.loadNewBot(botName);
            }
        }
    }

    @EventHandler
    public void onWorldUnload(WorldUnloadEvent event) {
        if (!addonsConfig.LEVELIZED_BOT_LOADING) {
            return;
        }
        bot2Level.entrySet().removeIf((e) -> {
            if (e.getValue().equals(event.getWorld())) {
                unloadedBots.computeIfAbsent(event.getWorld().getUID(), v -> new ArrayList<>()).add(e.getKey());
                return true;
            }
            return false;
        });
    }

    private UUID getBotLevel(String botName) {
        UUID uuid = BotUtil.getBotUUID(botName);
        ServerBot bot = new ServerBot(MinecraftServer.getServer(), MinecraftServer.getServer().getLevel(Level.OVERWORLD), new GameProfile(uuid, botName));
        Optional<CompoundTag> optional = storage.load(bot);
        if (optional.isPresent() && optional.get().contains("WorldUUIDMost") && optional.get().contains("WorldUUIDLeast")) {
            return new UUID(optional.get().getLong("WorldUUIDMost"), optional.get().getLong("WorldUUIDLeast"));
        }
        return null;
    }

}

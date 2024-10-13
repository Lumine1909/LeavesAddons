package io.github.lumine1909.listener;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.data.auth.PlayerCache;
import fr.xephi.authme.listener.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.leavesmc.leaves.event.bot.BotJoinEvent;
import org.leavesmc.leaves.event.bot.BotRemoveEvent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Locale;

import static io.github.lumine1909.LeavesAddons.addonsConfig;
import static io.github.lumine1909.LeavesAddons.plugin;

public class BotFixListener implements Listener {

    private final boolean isAuthmeEnabled;
    private PlayerCache cache;

    public BotFixListener() {
        AuthMe authMe = (AuthMe) Bukkit.getPluginManager().getPlugin("Authme");
        isAuthmeEnabled = authMe != null;
        if (!isAuthmeEnabled) {
            return;
        }
        try {
            final Class<?> listenerServiceC = Class.forName("fr.xephi.authme.listener.ListenerService");
            final Field injectorF = AuthMe.class.getDeclaredField("injector");
            injectorF.setAccessible(true);
            final Object injector = injectorF.get(authMe);
            final Method getSingletonM = injector.getClass().getDeclaredMethod("getSingleton", Class.class);
            getSingletonM.setAccessible(true);
            final Field listenerServiceF = PlayerListener.class.getDeclaredField("listenerService");
            listenerServiceF.setAccessible(true);
            final Field playerCacheF = listenerServiceC.getDeclaredField("playerCache");
            playerCacheF.setAccessible(true);
            cache = (PlayerCache) playerCacheF.get(listenerServiceF.get(getSingletonM.invoke(injector, PlayerListener.class)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBotJoin(BotJoinEvent e) {
        if (!isAuthmeEnabled || !addonsConfig.AUTHME_BOT_SUPPORT) {
            return;
        }
        cache.updatePlayer(PlayerAuth.builder().name(e.getBot().getName().toLowerCase(Locale.ROOT)).build());
    }

    @EventHandler
    public void onBotRemove(BotRemoveEvent e) {
        if (!isAuthmeEnabled || !addonsConfig.AUTHME_BOT_SUPPORT) {
            return;
        }
        cache.removePlayer(e.getBot().getName().toLowerCase(Locale.ROOT));
    }
}

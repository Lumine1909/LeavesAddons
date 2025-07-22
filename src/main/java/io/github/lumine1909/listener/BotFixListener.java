package io.github.lumine1909.listener;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.data.auth.PlayerCache;
import fr.xephi.authme.listener.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.leavesmc.leaves.event.bot.BotJoinEvent;
import org.leavesmc.leaves.event.bot.BotRemoveEvent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import static io.github.lumine1909.LeavesAddons.addonsConfig;
import static io.github.lumine1909.LeavesAddons.plugin;

public class BotFixListener implements Listener {

    private final boolean hasAuthme;
    private final AuthMe authMe;
    private boolean isAuthmeEnabled;
    private PlayerCache cache;
    private final List<PlayerAuth> preLoadBots = new ArrayList<>();

    public BotFixListener() {
        authMe = (AuthMe) Bukkit.getPluginManager().getPlugin("Authme");
        hasAuthme = authMe != null;
        if (!hasAuthme) {
            return;
        }
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPluginEnable(PluginEnableEvent e) {
        if (hasAuthme && e.getPlugin().getName().equalsIgnoreCase("AuthMe")) {
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
                isAuthmeEnabled = true;
                cachePreLoadBots();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @EventHandler
    public void onBotJoin(BotJoinEvent e) {
        if (!hasAuthme || !addonsConfig.AUTHME_BOT_SUPPORT) {
            return;
        }
        PlayerAuth auth = PlayerAuth.builder().name(e.getBot().getName().toLowerCase(Locale.ROOT)).build();
        if (!isAuthmeEnabled) {
            preLoadBots.add(auth);
            return;
        }
        cache.updatePlayer(auth);
    }

    @EventHandler
    public void onBotRemove(BotRemoveEvent e) {
        if (!hasAuthme || !addonsConfig.AUTHME_BOT_SUPPORT) {
            return;
        }
        cache.removePlayer(e.getBot().getName().toLowerCase(Locale.ROOT));
    }

    private void cachePreLoadBots() {
        for (Iterator<PlayerAuth> it = preLoadBots.iterator(); it.hasNext(); ) {
            PlayerAuth auth = it.next();
            cache.updatePlayer(auth);
            it.remove();
        }
    }
}

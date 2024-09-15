package io.github.lumine1909;

import io.github.lumine1909.command.CommandOverrideHandler;
import io.github.lumine1909.config.AddonsConfigHandler;
import io.github.lumine1909.listener.BotFixListener;
import io.github.lumine1909.listener.ShearsWrenchListener;
import io.github.lumine1909.listener.ShulkerStackListener;
import io.github.lumine1909.listener.WitherRosePlaceListener;
import org.bukkit.plugin.java.JavaPlugin;

public class LeavesAddons extends JavaPlugin {

    public static LeavesAddons instance;
    public static AddonsConfigHandler config;
    public static CommandOverrideHandler commandOverrideHandler;

    @Override
    public void onEnable() {
        instance = this;
        config = new AddonsConfigHandler();
        new BotFixListener();
        new ShearsWrenchListener();
        new WitherRosePlaceListener();
        new ShulkerStackListener();
    }
}

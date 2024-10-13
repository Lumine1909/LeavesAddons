package io.github.lumine1909;

import io.github.lumine1909.command.CommandOverrideHandler;
import io.github.lumine1909.config.AddonsConfigHandler;
import io.github.lumine1909.config.LeavesConfigHandler;
import io.github.lumine1909.listener.BotFixListener;
import io.github.lumine1909.listener.ShearsWrenchListener;
import io.github.lumine1909.listener.StackViewListener;
import io.github.lumine1909.listener.WitherRosePlaceListener;
import org.bukkit.plugin.java.JavaPlugin;

public class LeavesAddons extends JavaPlugin {

    public static LeavesAddons plugin;
    public static AddonsConfigHandler addonsConfig;
    public static LeavesConfigHandler leavesConfig;
    public static CommandOverrideHandler commandOverrideHandler;

    @Override
    public void onEnable() {
        plugin = this;
        addonsConfig = new AddonsConfigHandler();
        leavesConfig = new LeavesConfigHandler();
        commandOverrideHandler = new CommandOverrideHandler();
        registerListeners();
    }

    private void registerListeners() {
        new BotFixListener();
        new ShearsWrenchListener();
        new WitherRosePlaceListener();
        new StackViewListener();
    }
}

package io.github.lumine1909;

import io.github.lumine1909.command.BotCreateCommand;
import io.github.lumine1909.command.CommandOverrideHandler;
import io.github.lumine1909.config.AddonsConfigHandler;
import io.github.lumine1909.listener.*;
import io.github.lumine1909.metrics.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public class LeavesAddons extends JavaPlugin {

    public static LeavesAddons plugin;
    public static AddonsConfigHandler addonsConfig;
    public static CommandOverrideHandler commandOverrideHandler;

    @Override
    public void onEnable() {
        plugin = this;
        addonsConfig = new AddonsConfigHandler();
        commandOverrideHandler = new CommandOverrideHandler();
        registerListeners();
        registerCommands();
        new Metrics(this, 26096);
    }

    private void registerListeners() {
        new BotFixListener();
        new WitherRosePlaceListener();
        new LevelizedBotListener();
        new BetterCreationListener();
        new CommandHandleListener(commandOverrideHandler);
    }

    private void registerCommands() {
        new BotCreateCommand();
    }
}

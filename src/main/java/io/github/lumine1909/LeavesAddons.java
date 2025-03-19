package io.github.lumine1909;

import io.github.lumine1909.command.BotCreateCommand;
import io.github.lumine1909.command.CommandOverrideHandler;
import io.github.lumine1909.config.AddonsConfigHandler;
import io.github.lumine1909.listener.*;
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
    }

    private void registerListeners() {
        new BotFixListener();
        new ShearsWrenchListener();
        new WitherRosePlaceListener();
        new StackViewListener();
        new LevelizedBotListener();
        new BetterCreationListener();
        new CommandHandleListener(commandOverrideHandler);
    }

    private void registerCommands() {
        new BotCreateCommand();
    }
}

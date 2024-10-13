package io.github.lumine1909.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import static io.github.lumine1909.LeavesAddons.plugin;

public class AddonsConfigHandler {

    private final File addonsFile = new File(plugin.getDataFolder().getAbsoluteFile().getParentFile().getParentFile(), "leaves-addons.yml");
    private final FileConfiguration addons;

    public boolean REDSTONE_SHEARS_WRENCH;
    public boolean BETTER_WITHER_ROSE_PLACE;
    public boolean AUTHME_BOT_SUPPORT;
    public boolean CLIENT_STACK_VIEW;

    public AddonsConfigHandler() {
        addons = YamlConfiguration.loadConfiguration(addonsFile);
        if (!addonsFile.exists()) {
            createNewConfig();
        }
        REDSTONE_SHEARS_WRENCH = addons.getBoolean("feature.redstone-shears-wrench", false);
        BETTER_WITHER_ROSE_PLACE  = addons.getBoolean("feature.better-wither-rose-place", false);
        AUTHME_BOT_SUPPORT = addons.getBoolean("fix.authme-bot-support", false);
        CLIENT_STACK_VIEW = addons.getBoolean("fix.client-stack-view", false);
    }

    private void createNewConfig() {
        try {
            InputStream inputStream = plugin.getResource("leaves-addons.yml");
            Files.write(addonsFile.toPath(), inputStream.readAllBytes(), StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

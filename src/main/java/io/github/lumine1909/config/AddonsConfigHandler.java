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

    public boolean BETTER_WITHER_ROSE_PLACE;
    public boolean AUTHME_BOT_SUPPORT;
    public boolean BETTER_BOT_CREATION;
    public boolean BOT_SKIN_INHERIT;

    public AddonsConfigHandler() {
        addons = YamlConfiguration.loadConfiguration(addonsFile);
        if (!addonsFile.exists()) {
            createNewConfig();
        }
        BETTER_WITHER_ROSE_PLACE = addons.getBoolean("feature.better-wither-rose-place", false);
        addons.set("feature.better-wither-rose-place", BETTER_WITHER_ROSE_PLACE);
        AUTHME_BOT_SUPPORT = addons.getBoolean("fix.authme-bot-support", false);
        addons.set("fix.authme-bot-support", AUTHME_BOT_SUPPORT);
        BETTER_BOT_CREATION = addons.getBoolean("feature.better-bot-creation", false);
        addons.set("feature.better-bot-creation", BETTER_BOT_CREATION);
        BOT_SKIN_INHERIT = addons.getBoolean("feature.bot-skin-inherit", false);
        addons.set("feature.bot-skin-inherit", BOT_SKIN_INHERIT);
        try {
            addons.save(addonsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createNewConfig() {
        try {
            InputStream inputStream = plugin.getResource("leaves-addons.yml");
            assert inputStream != null;
            Files.write(addonsFile.toPath(), inputStream.readAllBytes(), StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

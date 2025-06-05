package io.github.lumine1909.command;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.leavesmc.leaves.LeavesConfig;
import org.leavesmc.leaves.bot.BotUtil;
import org.leavesmc.leaves.entity.BotCreator;
import org.leavesmc.leaves.entity.BotManager;

import java.util.*;

import static io.github.lumine1909.LeavesAddons.*;
import static net.kyori.adventure.text.Component.text;

public class BotCreateCommand implements CommandOverrider {

    private static final String BASE_PERM = "bukkit.command.bot.";
    private static final UUID DEFAULT_UUID = new UUID(114514L, 1919810L);

    public static final Map<UUID, Integer> creator2BotCountMap = new HashMap<>();
    public static ArrayList<String> pendingBots = new ArrayList<>();

    public BotCreateCommand() {
        commandOverrideHandler.register("bot", this, true);
    }

    @Override
    public List<String> onTabComplete(String command, CommandSender sender, String[] args) {
        return null;
    }

    @Override
    public boolean onCommand(String command, CommandSender sender, String[] args) {
        if (!addonsConfig.BETTER_BOT_CREATION) {
            return false;
        }
        if (args.length < 2 || !args[0].equals("create")) {
            return false;
        }
        String botName = args[1];
        String fullName = BotUtil.getFullName(botName);
        if (this.canCreate(sender, fullName)) {
            BotCreator creator = BotCreator.of(botName, Bukkit.getWorlds().getFirst().getSpawnLocation()).creator(sender);
            if (args.length >= 3) {
                creator.skinName(args[2]);
            }

            if (sender instanceof Player player) {
                creator.location(player.getLocation());
            } else if (sender instanceof ConsoleCommandSender) {
                if (args.length >= 7) {
                    try {
                        World world = Bukkit.getWorld(args[3]);
                        double x = Double.parseDouble(args[4]);
                        double y = Double.parseDouble(args[5]);
                        double z = Double.parseDouble(args[6]);
                        if (world != null) {
                            creator.location(new Location(world, x, y, z));
                        }
                    } catch (Exception e) {

                    }
                }
            }
            pendingBots.add(botName);
            creator.spawnWithSkin(null);
        }
        return true;
    }

    private int getMaxCreatableSize(CommandSender sender) {
        if (sender.hasPermission(BASE_PERM + "unlimited")) {
            return Integer.MAX_VALUE;
        }
        for (int i = 32; i > 0; i--) {
            if (sender.hasPermission(BASE_PERM + i)) {
                return i;
            }
        }
        return LeavesConfig.modify.fakeplayer.limit - Bukkit.getBotManager().getBots().size();
    }

    private boolean canCreate(CommandSender sender, @NotNull String name) {
        BotManager manager = Bukkit.getBotManager();
        if (!name.matches("^[a-zA-Z0-9_]{4,16}$")) {
            sender.sendMessage(text("This name is illegal", NamedTextColor.RED));
            return false;
        }

        if (Bukkit.getPlayerExact(name) != null || manager.getBot(name) != null) {
            sender.sendMessage(text("This player is already in server", NamedTextColor.RED));
            return false;
        }

        if (LeavesConfig.modify.fakeplayer.unableNames.contains(name)) {
            sender.sendMessage(text("This name is not allowed", NamedTextColor.RED));
            return false;
        }

        if (creator2BotCountMap.computeIfAbsent(getSenderUUID(sender), uuid -> 0) >= getMaxCreatableSize(sender)) {
            sender.sendMessage(text("Fakeplayer limit is full", NamedTextColor.RED));
            return false;
        }

        return true;
    }

    private UUID getSenderUUID(CommandSender sender) {
        if (sender instanceof Player player) {
            return player.getUniqueId();
        }
        return DEFAULT_UUID;
    }
}
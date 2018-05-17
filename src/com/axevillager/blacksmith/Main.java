package com.axevillager.blacksmith;

import com.axevillager.blacksmith.listeners.SmithingHandler;
import com.axevillager.blacksmith.player.CustomPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import static com.axevillager.blacksmith.player.CustomPlayer.getCustomPlayers;

/**
 * Main created by AxeVillager on 2018/04/20.
 */

public class Main extends JavaPlugin {

    private final String pluginName = this.toString();
    private final ConsoleCommandSender console = Bukkit.getConsoleSender();
    private final ChatColor yellow = ChatColor.YELLOW;
    public static Plugin plugin;

    public Main() {
        plugin = this;
    }

    @Override
    public void onEnable() {
        console.sendMessage(yellow + pluginName + " is loading...");
        saveDefaultConfig();
        registerListeners();
        loadCustomPlayers();
        console.sendMessage(ChatColor.GREEN + pluginName + " has been enabled.");
    }

    @Override
    public void onDisable() {
        console.sendMessage(yellow + pluginName + " is being disabled...");
        endAllSmithingProgresses();
        console.sendMessage(ChatColor.RED + pluginName + " has been disabled.");
    }

    private void registerListeners() {
        final PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new SmithingHandler(), this);
    }

    private void loadCustomPlayers() {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            new CustomPlayer(player.getUniqueId());
        }
    }

    private void endAllSmithingProgresses() {
        for (final CustomPlayer customPlayer : getCustomPlayers()) {
            if (customPlayer.isSmithing()) {
                customPlayer.stopSmithing();
            }
        }
    }
}
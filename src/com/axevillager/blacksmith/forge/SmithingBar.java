package com.axevillager.blacksmith.forge;

import com.axevillager.blacksmith.player.CustomPlayer;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import static com.axevillager.blacksmith.Main.plugin;

/**
 * SmithingBar created by AxeVillager on 2018/04/20.
 */

public class SmithingBar extends BukkitRunnable {

    // Interval should be 1.

    private BossBar bar;
    private final CustomPlayer customPlayer;
    private final String heatItemText;
    private final String repairItemText;
    private final String coolItemText;

    public SmithingBar(final CustomPlayer customPlayer) {
        final FileConfiguration config = plugin.getConfig();
        this.heatItemText = config.getString("heat item bar text");
        this.repairItemText = config.getString("repair item bar text");
        this.coolItemText = config.getString("cool item bar text");
        this.bar = Bukkit.createBossBar(this.heatItemText, BarColor.GREEN, BarStyle.SOLID);
        this.customPlayer = customPlayer;
        this.bar.addPlayer(this.customPlayer.getPlayer());
        this.bar.setVisible(true);
        this.customPlayer.setSmithingBar(this);
    }

    private void updateProgress() {
        final double progress = this.bar.getProgress();
        if (progress > 0.0033) {
            this.bar.setProgress(this.bar.getProgress() - 0.0033);
            return;
        }
        this.bar.setProgress(0);
    }

    private void updateColour() {
        final double progress = this.bar.getProgress();
        final double HALF = 0.5;
        final double FOURTH = 0.25;
        if (progress > HALF) {
            this.bar.setColor(BarColor.GREEN);
            return;
        }
        if (progress > FOURTH) {
            this.bar.setColor(BarColor.YELLOW);
            return;
        }
        this.bar.setColor(BarColor.RED);
    }

    private void updateText() {
        if (this.customPlayer.hasRepairedItem()) {
            this.bar.setTitle(this.coolItemText);
            return;
        }
        if (this.customPlayer.hasHeatedItem()) {
            this.bar.setTitle(this.repairItemText);
            return;
        }
        if (!this.customPlayer.hasHeatedItem()) {
            this.bar.setTitle(this.heatItemText);
            return;
        }
        this.bar.setTitle("This is an error. Please contact the management if this happens regularly.");
    }

    private boolean isOver() {
        return this.customPlayer == null || this.customPlayer.getSmithingBar() == null || !this.customPlayer.isSmithing() || this.customPlayer.hasCooledDownItem() || this.bar.getProgress() <= 0;
    }

    public void clear() {
        this.bar.removeAll();
        this.bar.setVisible(false);
        this.bar = null;
    }

    private void stop() {
        this.customPlayer.stopSmithing();
    }

    @Override
    public void run() {
        if (isOver()) {
            this.cancel();
            stop();
        }
        if (!this.isCancelled()) {
            updateProgress();
            updateColour();
            updateText();
        }
    }
}
package com.axevillager.blacksmith.forge;

import com.axevillager.blacksmith.player.CustomPlayer;
import org.bukkit.*;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * AnvilRepairingEffects created by AxeVillager on 2018/04/21.
 */

public class AnvilRepairingEffects extends BukkitRunnable {

    // Interval should be 13.

    private final CustomPlayer customPlayer;
    private final Location location;
    private int counter;

    public AnvilRepairingEffects(final CustomPlayer customPlayer, final Location location) {
        this.customPlayer = customPlayer;
        this.location = location;
    }

    private void playAnvilSounds() {
        location.getWorld().playSound(location, Sound.BLOCK_ANVIL_USE, 3, 0);
    }

    private void showAnvilEffects() {
        location.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, location, 3);
    }

    @Override
    public void run() {
        if (counter == 0) {
            if (this.customPlayer.isSmithing()) {
                this.customPlayer.setIsRepairingItem(true);
            }
            playAnvilSounds();
        }
        if (counter >= 3) {
            if (this.customPlayer.isSmithing()) {
                this.customPlayer.setHasRepairedItem(true);
            }
            this.cancel();
        }
        showAnvilEffects();
        counter++;
    }
}
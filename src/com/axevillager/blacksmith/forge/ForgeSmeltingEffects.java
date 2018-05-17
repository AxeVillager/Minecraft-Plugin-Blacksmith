package com.axevillager.blacksmith.forge;

import com.axevillager.blacksmith.player.CustomPlayer;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * ForgeSmeltingEffects created by AxeVillager on 2018/04/21.
 */

public class ForgeSmeltingEffects extends BukkitRunnable {

    // Interval should be 5.

    private final CustomPlayer customPlayer;
    private final Location location;
    private int counter;

    public ForgeSmeltingEffects(final CustomPlayer customPlayer, final Location location) {
        this.customPlayer = customPlayer;
        this.location = location;
    }

    private void showSmeltingParticles() {
        final World world = location.getWorld();
        world.spawnParticle(Particle.LAVA, location, 8, 0.05, 0.05, 0.05);
        world.spawnParticle(Particle.SMOKE_NORMAL, location, 1);
    }

    private void playSmeltingSounds() {
        final World world = location.getWorld();
        world.playSound(location, Sound.BLOCK_FURNACE_FIRE_CRACKLE, 3, 1);
        world.playSound(location, Sound.BLOCK_FIRE_AMBIENT, 3, 1);
    }

    private void playSmeltingEffects() {
        showSmeltingParticles();
        playSmeltingSounds();
    }

    @Override
    public void run() {
        if (counter == 24) {
            customPlayer.setHasHeatedItem(true);
            this.cancel();
        }
        playSmeltingEffects();
        counter++;
    }
}
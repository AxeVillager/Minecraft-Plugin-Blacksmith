package com.axevillager.blacksmith.listeners;

import com.axevillager.blacksmith.forge.AnvilRepairingEffects;
import com.axevillager.blacksmith.forge.Forge;
import com.axevillager.blacksmith.forge.ForgeSmeltingEffects;
import com.axevillager.blacksmith.forge.SmithingBar;
import com.axevillager.blacksmith.player.CustomPlayer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Cauldron;

import static com.axevillager.blacksmith.Main.plugin;
import static com.axevillager.blacksmith.player.CustomPlayer.getCustomPlayer;
import static com.axevillager.blacksmith.utilities.StringUtils.determineAnOrA;
import static com.axevillager.blacksmith.utilities.StringUtils.fixMaterialName;
import static org.bukkit.Material.*;

/**
 * SmithingHandler created by AxeVillager on 2018/04/21.
 */

public class SmithingHandler implements Listener {

    private final Forge forge = new Forge();
    private final Material[] repairableMaterials = {SHIELD,
            IRON_SWORD, IRON_AXE, IRON_PICKAXE, IRON_SPADE, IRON_HOE,
            IRON_BOOTS, IRON_LEGGINGS, IRON_CHESTPLATE, IRON_HELMET,
            CHAINMAIL_BOOTS, CHAINMAIL_LEGGINGS, CHAINMAIL_CHESTPLATE, CHAINMAIL_HELMET,
            GOLD_SWORD, GOLD_AXE, GOLD_PICKAXE, GOLD_SPADE, GOLD_HOE,
            GOLD_BOOTS, GOLD_LEGGINGS, GOLD_CHESTPLATE, GOLD_HELMET,
            DIAMOND_SWORD, DIAMOND_AXE, DIAMOND_PICKAXE, DIAMOND_SPADE, DIAMOND_HOE,
            DIAMOND_BOOTS, DIAMOND_LEGGINGS, DIAMOND_CHESTPLATE, DIAMOND_HELMET};


    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {

        final Player player = event.getPlayer();
        CustomPlayer customPlayer = getCustomPlayer(player.getUniqueId());
        final ItemStack itemInMainHand = player.getInventory().getItemInMainHand();

        if (customPlayer == null) {
            customPlayer = new CustomPlayer(player.getUniqueId());
        }

        if (!customPlayer.isSmith() || itemInMainHand == null || !itemTypeIsRepairable(itemInMainHand.getType()) || event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        final Block clickedBlock = event.getClickedBlock();
        final Location clickedBlockLocation = clickedBlock.getLocation();
        final Material clickedBlockType = clickedBlock.getType();

        if (!customPlayer.isSmithing()) {
            if (clickedBlockType.equals(forge.getForgeBlockType())) {

                final Location forgeLocation = forge.candidateForgeLocation(clickedBlockLocation);

                if (forgeLocation == null || !forge.isForge(forgeLocation)) {
                    return;
                }

                event.setCancelled(true);

                final double x = forgeLocation.getX() + 0.5;
                final double y = forgeLocation.getY();
                final double z = forgeLocation.getZ() + 0.5;
                final Location forgeEffectLocation = new Location(forgeLocation.getWorld(), x, y, z);

                customPlayer.setRepairingItem(itemInMainHand);
                customPlayer.setIsSmithing(true);
                new SmithingBar(customPlayer).runTaskTimer(plugin, 1, 1);
                new ForgeSmeltingEffects(customPlayer, forgeEffectLocation).runTaskTimer(plugin, 5, 5); // Heating item takes 120 ticks (6 seconds).
            }
        }

        if (!customPlayer.isSmithing()) {
            return;
        }

        final Location effectsLocation = new Location(clickedBlock.getWorld(), clickedBlockLocation.getX() + 0.5, clickedBlockLocation.getY() + 0.5, clickedBlockLocation.getZ() + 0.5);
        final String itemName = fixMaterialName(customPlayer.getRepairingItem().getType());

        if (clickedBlockType.equals(forge.getForgeBlockType())) {
            event.setCancelled(true);
            return;
        }

        if (clickedBlockType.equals(ANVIL)) {

            event.setCancelled(true);

            if (!customPlayer.hasHeatedItem() || customPlayer.isRepairingItem()) {
                return;
            }

            if (!customPlayer.getRepairingItem().equals(itemInMainHand)) {
                customPlayer.sendError("You must use the " + itemName + " you heated in the forge.");
                return;
            }

            if (itemInMainHand.getDurability() <= 0) {
                customPlayer.sendError("There is no need to repair an item that is not damaged.");
                return;
            }

            if (!customPlayer.hasRepairingResource()) {
                final String resourceName = fixMaterialName(customPlayer.repairingResourceNeeded());
                customPlayer.sendError("You must carry " + determineAnOrA(resourceName) + " " + resourceName + " in your inventory to repair your " + itemName + ".");
                return;
            }

            customPlayer.removeOneResource();
            new AnvilRepairingEffects(customPlayer, effectsLocation).runTaskTimer(plugin, 0, 13);
        }

        if (clickedBlockType.equals(CAULDRON)) {

            event.setCancelled(true);

            if (!customPlayer.hasRepairedItem() && !customPlayer.hasCooledDownItem()) {
                return;
            }

            if (!customPlayer.getRepairingItem().equals(itemInMainHand)) {
                customPlayer.sendError("You must use the " + itemName + " you repaired on the anvil.");
                return;
            }

            final BlockState cauldronState = clickedBlock.getState();
            final Cauldron cauldronData = (Cauldron) cauldronState.getData();

            if (cauldronData.isEmpty()) {
                customPlayer.sendError("There must be water in the cauldron for the " + itemName + " to cool down.");
                return;
            }

            final short newDurability = calculateNewDurability(itemInMainHand);
            itemInMainHand.setDurability(newDurability);
            if (itemInMainHand.getDurability() < 0) {
                itemInMainHand.setDurability((short) 0);
            }

            playCauldronEffects(effectsLocation);
            cauldronData.setData((byte) (cauldronData.getData() - 1));
            cauldronState.update();

            customPlayer.setHasCooledDownItem(true);
            customPlayer.setIsSmithing(false);

            executeCommand(player.getName());
        }
    }

    private void executeCommand(final String playerName) {
        String command = plugin.getConfig().getString("command");
        if (command == null || command.equals(""))
            return;

        command = command.replace("PLAYER_NAME", playerName);
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
    }

    private short calculateNewDurability(final ItemStack item) {
        final Material itemType = item.getType();
        final short maxDurability = itemType.getMaxDurability();
        final short repairAmount = (short) ((maxDurability * getFactor(itemType)));
        return (short) (item.getDurability() - repairAmount);
    }

    private void playCauldronEffects(final Location location) {
        final World world = location.getWorld();
        world.spawnParticle(Particle.SPIT, location, 3);
        world.spawnParticle(Particle.SMOKE_NORMAL, location, 1);
        world.spawnParticle(Particle.DRIP_WATER, location, 2, 0.05, 0.2, 0.05);
        world.playSound(location, Sound.BLOCK_REDSTONE_TORCH_BURNOUT, 3, 0);
    }

    private boolean itemTypeIsRepairable(final Material itemType) {
        for (final Material repairableMaterial : repairableMaterials)
            if (itemType.equals(repairableMaterial))
                return true;
        return false;
    }

    private double getFactor(final Material material) {
        final String materialName = material.name().toLowerCase();
        if (materialName.contains("axe")) {
            return 0.5;
        } else if (materialName.contains("spade")) {
            return 1;
        } else if (materialName.contains("sword") || materialName.contains("hoe")) {
            return 1;
        } else if (materialName.contains("boots")) {
            return 0.34;
        } else if (materialName.contains("leggings")) {
            return 0.2;
        } else if (materialName.contains("chestplate")) {
            return 0.143;
        } else if (materialName.contains("helmet")) {
            return 0.25;
        } else if (materialName.contains("shield")) {
            return 1;
        } else {
            return 0.5;
        }
    }

    @EventHandler
    public void onLeave(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final CustomPlayer customPlayer = getCustomPlayer(player.getUniqueId());

        if (customPlayer == null) {
            return;
        }

        if (customPlayer.isSmithing()) {
            customPlayer.stopSmithing();
        }

        customPlayer.remove();
    }
}
package com.axevillager.blacksmith.player;

import com.axevillager.blacksmith.forge.SmithingBar;
import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * CustomPlayer created by AxeVillager on 2018/04/20.
 */

public class CustomPlayer {

    private final static ArrayList<CustomPlayer> CUSTOM_PLAYERS_LIST = new ArrayList<>();
    private final UUID uuid;
    private final Player player;
    private boolean smithing;
    private boolean heatedItem;
    private boolean isRepairingItem;
    private boolean repairedItem;
    private boolean cooledDownItem;
    private Long errorTime = 0L;
    private ItemStack repairingItem;
    private SmithingBar smithingBar;

    public CustomPlayer(final UUID uuid) {
        this.uuid = uuid;
        this.player = Bukkit.getPlayer(this.uuid);
        CUSTOM_PLAYERS_LIST.add(this);
    }

    public static CustomPlayer getCustomPlayer(final UUID uuid) {
        for (final CustomPlayer customPlayer : getCustomPlayers()) {
            if (customPlayer.getUniqueId().equals(uuid)) {
                return customPlayer;
            }
        }
        return null;
    }

    public static List<CustomPlayer> getCustomPlayers() {
        return ImmutableList.copyOf(CUSTOM_PLAYERS_LIST);
    }

    public void remove() {
        CUSTOM_PLAYERS_LIST.remove(this);
    }

    private UUID getUniqueId() {
        return this.uuid;
    }

    public Player getPlayer() {
        return this.player;
    }

    public boolean isSmith() {
        return this.player.hasPermission("blacksmith.use");
    }

    public void setIsSmithing(final boolean value) {
        this.smithing = value;
    }

    public boolean isSmithing() {
        return this.smithing;
    }

    public void setHasHeatedItem(final boolean value) {
        this.heatedItem = value;
    }

    public boolean hasHeatedItem() {
        return this.heatedItem;
    }

    public void setIsRepairingItem(final boolean value) {
        this.isRepairingItem = value;
    }

    public boolean isRepairingItem() {
        return this.isRepairingItem;
    }

    public void setHasRepairedItem(final boolean value) {
        this.repairedItem = value;
    }

    public boolean hasRepairedItem() {
        return this.repairedItem;
    }

    public void setHasCooledDownItem(final boolean value) {
        this.cooledDownItem = value;
    }

    public boolean hasCooledDownItem() {
        return this.cooledDownItem;
    }

    public void setRepairingItem(final ItemStack item) {
        this.repairingItem = item;
    }

    public ItemStack getRepairingItem() {
        return this.repairingItem;
    }

    public void sendError(final String message) {
        if (canReceiveError()) {
            this.player.sendMessage(ChatColor.RED + message);
            this.errorTime = System.currentTimeMillis();
        }
    }

    private boolean canReceiveError() {
        return this.errorTime < System.currentTimeMillis() - 1000;
    }

    public Material repairingResourceNeeded() {
        return getResource(this.repairingItem.getType());
    }

    public boolean hasRepairingResource() {
        return this.player.getInventory().contains(repairingResourceNeeded());
    }

    public void removeOneResource() {
        final ItemStack resource = new ItemStack(getResource(this.repairingItem.getType()), 1);
        this.player.getInventory().removeItem(resource);
        this.player.updateInventory();
    }

    public void setSmithingBar(final SmithingBar smithingBar) {
        this.smithingBar = smithingBar;
    }

    public SmithingBar getSmithingBar() {
        return this.smithingBar;
    }

    public void stopSmithing() {
        setIsSmithing(false);
        setHasHeatedItem(false);
        setIsRepairingItem(false);
        setHasRepairedItem(false);
        setHasCooledDownItem(false);
        setRepairingItem(null);
        this.smithingBar.cancel();
        this.smithingBar.clear();
        setSmithingBar(null);
    }

    private Material getResource(final Material material) {
        final String materialName = material.name().toLowerCase();
        if (materialName.contains("iron")) {
            return Material.IRON_INGOT;
        } else if (materialName.contains("gold")) {
            return Material.GOLD_INGOT;
        } else if (materialName.contains("diamond")) {
            return Material.DIAMOND;
        } else if (materialName.contains("chainmail")) {
            return Material.IRON_INGOT;
        } else if (materialName.contains("shield")) {
            return Material.IRON_INGOT;
        } else {
            return Material.IRON_INGOT;
        }
    }
}
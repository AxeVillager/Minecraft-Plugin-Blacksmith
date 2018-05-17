package com.axevillager.blacksmith.forge;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import static com.axevillager.blacksmith.Main.plugin;
import static org.bukkit.Material.*;

/**
 * Forge created by AxeVillager on 2018/04/20.
 */

public class Forge {

    private final Material FORGE_BLOCK_TYPE;
    private int x;
    private int y;
    private int z;

    public Forge() {
        final Material material = getMaterial(plugin.getConfig().getString("forge item"));
        if (material == null) {
            Bukkit.getLogger().warning("Forge item is invalid. Using default (iron block) instead.");
            this.FORGE_BLOCK_TYPE = Material.IRON_BLOCK;
            return;
        }
        this.FORGE_BLOCK_TYPE = material;
    }

    public Material getForgeBlockType() {
        return this.FORGE_BLOCK_TYPE;
    }

    public Location candidateForgeLocation(final Location location) {
        final World world = location.getWorld();
        final double locX = location.getX();
        final double locY = location.getY();
        final double locZ = location.getZ();
        final Location loc1 = new Location(world, locX - 1, locY - 1, locZ - 1);
        final Location loc2 = new Location(world, locX + 1, locY + 1, locZ + 1);
        final int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        final int minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        final int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        final int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        final int maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
        final int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    final Material blockType = world.getBlockAt(x, y, z).getType();
                    if (blockType == FIRE) {
                        return new Location(world, x, y, z);
                    }
                }
            }
        }
        return null;
    }

    public boolean isForge(final Location location) {
        return forgeIsFacingNorth(location) || forgeIsFacingEast(location) || forgeIsFacingSouth(location) || forgeIsFacingWest(location);
    }

    private boolean forgeIsFacingNorth(final Location location) {
        final World world = location.getWorld();
        x = (int) location.getX();
        y = (int) location.getY();
        z = (int) location.getZ() + 1;
        return STAIRS_DOWN_EAST(materialAt(world, x + -1, y + -1, z + -2), blockDataAt(world, x + -1, y + -1, z + -2)) &&
                FULL_BLOCK(materialAt(world, x + -1, y + -1, z + -1)) &&
                FULL_BLOCK(materialAt(world, x + -1, y + -1, z)) &&
                STAIRS_UP_EAST(materialAt(world, x + -1, y, z + -2), blockDataAt(world, x + -1, y, z + -2)) &&
                FULL_BLOCK(materialAt(world, x + -1, y, z + -1)) &&
                STAIRS_DOWN_EAST(materialAt(world, x + -1, y, z), blockDataAt(world, x + -1, y, z)) &&
                STAIRS_DOWN_EAST(materialAt(world, x + -1, y + 1, z + -1), blockDataAt(world, x + -1, y + 1, z + -1)) &&
                SLAB_UP(materialAt(world, x, y + -1, z + -2), blockDataAt(world, x, y + -1, z + -2)) &&
                FULL_BLOCK(materialAt(world, x, y + -1, z)) &&
                materialAt(world, x, y, z + -1) == FIRE &&
                materialAt(world, x, y, z) == FORGE_BLOCK_TYPE &&
                SLAB_DOWN(materialAt(world, x, y + 1, z + -2), blockDataAt(world, x, y + 1, z + -2)) &&
                FULL_BLOCK(materialAt(world, x, y + 1, z + -1)) &&
                STAIRS_DOWN_NORTH(materialAt(world, x, y + 1, z), blockDataAt(world, x, y + 1, z)) &&
                FULL_BLOCK(materialAt(world, x, y + 2, z + -1)) &&
                STAIRS_DOWN_WEST(materialAt(world, x + 1, y + -1, z + -2), blockDataAt(world, x + 1, y + -1, z + -2)) &&
                FULL_BLOCK(materialAt(world, x + 1, y + -1, z + -1)) &&
                FULL_BLOCK(materialAt(world, x + 1, y + -1, z)) &&
                STAIRS_UP_WEST(materialAt(world, x + 1, y, z + -2), blockDataAt(world, x + 1, y, z + -2)) &&
                FULL_BLOCK(materialAt(world, x + 1, y, z + -1)) &&
                STAIRS_DOWN_WEST(materialAt(world, x + 1, y, z), blockDataAt(world, x + 1, y, z)) &&
                STAIRS_DOWN_WEST(materialAt(world, x + 1, y + 1, z + -1), blockDataAt(world, x + 1, y + 1, z + -1));
    }

    private boolean forgeIsFacingEast(final Location location) {
        final World world = location.getWorld();
        x = (int) location.getX() - 1;
        y = (int) location.getY();
        z = (int) location.getZ();
        return FULL_BLOCK(materialAt(world, x, y + -1, z + -1)) &&
                FULL_BLOCK(materialAt(world, x, y + -1, z)) &&
                FULL_BLOCK(materialAt(world, x, y + -1, z + 1)) &&
                STAIRS_DOWN_SOUTH(materialAt(world, x, y, z + -1), blockDataAt(world, x, y, z + -1)) &&
                materialAt(world, x, y, z) == FORGE_BLOCK_TYPE &&
                STAIRS_DOWN_NORTH(materialAt(world, x, y, z + 1), blockDataAt(world, x, y, z + 1)) &&
                STAIRS_DOWN_EAST(materialAt(world, x, y + 1, z), blockDataAt(world, x, y + 1, z)) &&
                FULL_BLOCK(materialAt(world, x + 1, y + -1, z + -1)) &&
                FULL_BLOCK(materialAt(world, x + 1, y + -1, z + 1)) &&
                FULL_BLOCK(materialAt(world, x + 1, y, z + -1)) &&
                materialAt(world, x + 1, y, z) == FIRE &&
                FULL_BLOCK(materialAt(world, x + 1, y, z + 1)) &&
                STAIRS_DOWN_SOUTH(materialAt(world, x + 1, y + 1, z + -1), blockDataAt(world, x + 1, y + 1, z + -1)) &&
                FULL_BLOCK(materialAt(world, x + 1, y + 1, z)) &&
                STAIRS_DOWN_NORTH(materialAt(world, x + 1, y + 1, z + 1), blockDataAt(world, x + 1, y + 1, z + 1)) &&
                FULL_BLOCK(materialAt(world, x + 1, y + 2, z)) &&
                STAIRS_DOWN_SOUTH(materialAt(world, x + 2, y + -1, z + -1), blockDataAt(world, x + 2, y + -1, z + -1)) &&
                SLAB_UP(materialAt(world, x + 2, y + -1, z), blockDataAt(world, x + 2, y + -1, z)) &&
                STAIRS_DOWN_NORTH(materialAt(world, x + 2, y + -1, z + 1), blockDataAt(world, x + 2, y + -1, z + 1)) &&
                STAIRS_UP_SOUTH(materialAt(world, x + 2, y, z + -1), blockDataAt(world, x + 2, y, z + -1)) &&
                STAIRS_UP_NORTH(materialAt(world, x + 2, y, z + 1), blockDataAt(world, x + 2, y, z + 1)) &&
                SLAB_DOWN(materialAt(world, x + 2, y + 1, z), blockDataAt(world, x + 2, y + 1, z));
    }

    private boolean forgeIsFacingSouth(final Location location) {
        final World world = location.getWorld();
        x = (int) location.getX();
        y = (int) location.getY();
        z = (int) location.getZ() - 1;
        return FULL_BLOCK(materialAt(world, x + -1, y + -1, z)) &&
                FULL_BLOCK(materialAt(world, x + -1, y + -1, z + 1)) &&
                STAIRS_DOWN_EAST(materialAt(world, x + -1, y + -1, z + 2), blockDataAt(world, x + -1, y + -1, z + 2)) &&
                STAIRS_DOWN_EAST(materialAt(world, x + -1, y, z), blockDataAt(world, x + -1, y, z)) &&
                FULL_BLOCK(materialAt(world, x + -1, y, z + 1)) &&
                STAIRS_UP_EAST(materialAt(world, x + -1, y, z + 2), blockDataAt(world, x + -1, y, z + 2)) &&
                STAIRS_DOWN_EAST(materialAt(world, x + -1, y + 1, z + 1), blockDataAt(world, x + -1, y + 1, z + 1)) &&
                FULL_BLOCK(materialAt(world, x, y + -1, z)) &&
                SLAB_UP(materialAt(world, x, y + -1, z + 2), blockDataAt(world, x, y + -1, z + 2)) &&
                materialAt(world, x, y, z) == FORGE_BLOCK_TYPE &&
                materialAt(world, x, y, z + 1) == FIRE &&
                STAIRS_DOWN_SOUTH(materialAt(world, x, y + 1, z), blockDataAt(world, x, y + 1, z)) &&
                FULL_BLOCK(materialAt(world, x, y + 1, z + 1)) &&
                SLAB_DOWN(materialAt(world, x, y + 1, z + 2), blockDataAt(world, x, y + 1, z + 2)) &&
                FULL_BLOCK(materialAt(world, x, y + 2, z + 1)) &&
                FULL_BLOCK(materialAt(world, x + 1, y + -1, z)) &&
                FULL_BLOCK(materialAt(world, x + 1, y + -1, z + 1)) &&
                STAIRS_DOWN_WEST(materialAt(world, x + 1, y + -1, z + 2), blockDataAt(world, x + 1, y + -1, z + 2)) &&
                STAIRS_DOWN_WEST(materialAt(world, x + 1, y, z), blockDataAt(world, x + 1, y, z)) &&
                FULL_BLOCK(materialAt(world, x + 1, y, z + 1)) &&
                STAIRS_UP_WEST(materialAt(world, x + 1, y, z + 2), blockDataAt(world, x + 1, y, z + 2)) &&
                STAIRS_DOWN_WEST(materialAt(world, x + 1, y + 1, z + 1), blockDataAt(world, x + 1, y + 1, z + 1));

    }

    private boolean forgeIsFacingWest(final Location location) {
        final World world = location.getWorld();
        x = (int) location.getX() + 1;
        y = (int) location.getY();
        z = (int) location.getZ() + 1;
        return STAIRS_DOWN_SOUTH(materialAt(world, x + -2, y + -1, z + -2), blockDataAt(world, x + -2, y + -1, z + -2)) &&
                SLAB_UP(materialAt(world, x + -2, y + -1, z + -1), blockDataAt(world, x + -2, y + -1, z + -1)) &&
                STAIRS_DOWN_NORTH(materialAt(world, x + -2, y + -1, z), blockDataAt(world, x + -2, y + -1, z)) &&
                STAIRS_UP_SOUTH(materialAt(world, x + -2, y, z + -2), blockDataAt(world, x + -2, y, z + -2)) &&
                STAIRS_UP_NORTH(materialAt(world, x + -2, y, z), blockDataAt(world, x + -2, y, z)) &&
                SLAB_DOWN(materialAt(world, x + -2, y + 1, z + -1), blockDataAt(world, x + -2, y + 1, z + -1)) &&
                FULL_BLOCK(materialAt(world, x + -1, y + -1, z + -2)) &&
                FULL_BLOCK(materialAt(world, x + -1, y + -1, z)) &&
                FULL_BLOCK(materialAt(world, x + -1, y, z + -2)) &&
                materialAt(world, x + -1, y, z + -1) == FIRE &&
                FULL_BLOCK(materialAt(world, x + -1, y, z)) &&
                STAIRS_DOWN_SOUTH(materialAt(world, x + -1, y + 1, z + -2), blockDataAt(world, x + -1, y + 1, z + -2)) &&
                FULL_BLOCK(materialAt(world, x + -1, y + 1, z + -1)) &&
                STAIRS_DOWN_NORTH(materialAt(world, x + -1, y + 1, z), blockDataAt(world, x + -1, y + 1, z)) &&
                FULL_BLOCK(materialAt(world, x + -1, y + 2, z + -1)) &&
                FULL_BLOCK(materialAt(world, x, y + -1, z + -2)) &&
                FULL_BLOCK(materialAt(world, x, y + -1, z + -1)) &&
                FULL_BLOCK(materialAt(world, x, y + -1, z)) &&
                STAIRS_DOWN_SOUTH(materialAt(world, x, y, z + -2), blockDataAt(world, x, y, z + -2)) &&
                materialAt(world, x, y, z + -1) == FORGE_BLOCK_TYPE &&
                STAIRS_DOWN_NORTH(materialAt(world, x, y, z), blockDataAt(world, x, y, z)) &&
                STAIRS_DOWN_WEST(materialAt(world, x, y + 1, z + -1), blockDataAt(world, x, y + 1, z + -1));
    }

    private Material materialAt(final World world, final int x, final int y, final int z) {
        return world.getBlockAt(x, y, z).getType();
    }

    @SuppressWarnings("deprecation")
    private byte blockDataAt(final World world, final int x, final int y, final int z) {
        return world.getBlockAt(x, y, z).getData();
    }

    private boolean SLAB_DOWN(final Material material, final byte data) {
        return material == Material.STEP && (data == 3 || data == 4 || data == 5);
    }

    private boolean SLAB_UP(final Material material, final byte data) {
        return material == Material.STEP && (data == 11 || data == 12 || data == 13);
    }

    private boolean FULL_BLOCK(final Material material) {
        return material == Material.COBBLESTONE || material == Material.SMOOTH_BRICK || material == Material.BRICK;
    }

    private boolean STAIRS_DOWN_EAST(final Material material, final byte data) {
        return (material == COBBLESTONE_STAIRS
                || material == SMOOTH_STAIRS
                || material == BRICK_STAIRS)
                && data == 0;
    }

    private boolean STAIRS_DOWN_WEST(final Material material, final byte data) {
        return (material == COBBLESTONE_STAIRS
                || material == SMOOTH_STAIRS
                || material == BRICK_STAIRS)
                && data == 1;
    }

    private boolean STAIRS_DOWN_SOUTH(final Material material, final byte data) {
        return (material == COBBLESTONE_STAIRS
                || material == SMOOTH_STAIRS
                || material == BRICK_STAIRS)
                && data == 2;
    }

    private boolean STAIRS_DOWN_NORTH(final Material material, final byte data) {
        return (material == COBBLESTONE_STAIRS
                || material == SMOOTH_STAIRS
                || material == BRICK_STAIRS)
                && data == 3;
    }

    private boolean STAIRS_UP_EAST(final Material material, final byte data) {
        return (material == COBBLESTONE_STAIRS
                || material == SMOOTH_STAIRS
                || material == BRICK_STAIRS)
                && data == 4;
    }

    private boolean STAIRS_UP_WEST(final Material material, final byte data) {
        return (material == COBBLESTONE_STAIRS
                || material == SMOOTH_STAIRS
                || material == BRICK_STAIRS)
                && data == 5;
    }

    private boolean STAIRS_UP_SOUTH(final Material material, final byte data) {
        return (material == COBBLESTONE_STAIRS
                || material == SMOOTH_STAIRS
                || material == BRICK_STAIRS)
                && data == 6;
    }

    private boolean STAIRS_UP_NORTH(final Material material, final byte data) {
        return (material == COBBLESTONE_STAIRS
                || material == SMOOTH_STAIRS
                || material == BRICK_STAIRS)
                && data == 7;
    }
}
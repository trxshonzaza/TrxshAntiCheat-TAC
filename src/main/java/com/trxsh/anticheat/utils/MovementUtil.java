package com.trxsh.anticheat.utils;

import org.bukkit.Location;
import org.bukkit.Material;

import com.trxsh.anticheat.Checks.MaterialCheck;

public class MovementUtil {

    public static final double PLAYER_WIDTH = .6;
    public static final double PLAYER_HEIGTH = 1.8;
    public static final double PLAYER_HEIGTH_SNEAKING = 1.6;
    private static final MaterialCheck CHECK_STAIR, CHECK_STEP;

    public static boolean isStepping(Location location) {
        return isColliding(location, CHECK_STAIR) || isColliding(location, CHECK_STEP);
    }

    public static boolean isColliding(Location location, MaterialCheck material) {
        double d = PLAYER_WIDTH/2;
        return material.checkMaterial(location)
                || material.checkMaterial(location.clone().add( d, 0, 0))
                || material.checkMaterial(location.clone().add(-d, 0, 0))
                || material.checkMaterial(location.clone().add( d, 0,  d))
                || material.checkMaterial(location.clone().add(-d, 0,  d))
                || material.checkMaterial(location.clone().add( d, 0, -d))
                || material.checkMaterial(location.clone().add(-d, 0, -d))
                || material.checkMaterial(location.clone().add(0,  0, -d))
                || material.checkMaterial(location.clone().add(0,  0,  d));
    }

    /**
     * This returns if the player should be flagged
     * @return true if the player is now on ground or if the player is on a vine or on a ladder (Liquids?)
     * */
    public static boolean shouldNotFlag(Location loc) {
        return isMaterialGlideable(loc.getBlock().getType())
                || isMaterialGlideable(loc.clone().add(PLAYER_WIDTH/2, 0, 0).getBlock().getType())
                || isMaterialGlideable(loc.clone().add(PLAYER_WIDTH/2, 0, PLAYER_WIDTH/2).getBlock().getType())
                || isMaterialGlideable(loc.clone().add(PLAYER_WIDTH/2, 0, -PLAYER_WIDTH/2).getBlock().getType())
                || isMaterialGlideable(loc.clone().add(-PLAYER_WIDTH/2, 0, 0).getBlock().getType())
                || isMaterialGlideable(loc.clone().add(-PLAYER_WIDTH/2, 0, -PLAYER_WIDTH/2).getBlock().getType())
                || isMaterialGlideable(loc.clone().add(0, 0, -PLAYER_WIDTH/2).getBlock().getType())
                || isMaterialGlideable(loc.clone().add(0, 0, PLAYER_WIDTH/2).getBlock().getType())
                || isOnGround(loc.clone().add(0, 0, 0));

    }

    public static boolean isOnGround(Location loc) {
        return loc.getBlock().getLocation().clone().add(0, -1, 0).getBlock().getType().isSolid() || loc.clone().add(PLAYER_WIDTH/2, -1,0).getBlock().getType().isSolid()
                || loc.clone().add(PLAYER_WIDTH/2, -1, PLAYER_WIDTH/2).getBlock().getType().isSolid()
                || loc.clone().add(PLAYER_WIDTH/2, -1, -PLAYER_WIDTH/2).getBlock().getType().isSolid()
                || loc.clone().add(-PLAYER_WIDTH/2, -1, 0).getBlock().getType().isSolid()
                || loc.clone().add(-PLAYER_WIDTH/2, -1,-PLAYER_WIDTH/2).getBlock().getType().isSolid()
                || loc.clone().add(0, -1,-PLAYER_WIDTH/2).getBlock().getType().isSolid()
                || loc.clone().add(0, -1, PLAYER_WIDTH/2).getBlock().getType().isSolid();
    }

    public static boolean isMaterialGlideable(Material mat) {
        // TODO Add liquids
        switch (mat) {
            case LADDER:
            case VINE:
            case WATER:
            case LAVA:
            case TWISTING_VINES:
            case CAVE_VINES:
            case TWISTING_VINES_PLANT:
            case WEEPING_VINES:
            case WEEPING_VINES_PLANT:
            case CAVE_VINES_PLANT:
                return true;
            default:
                return false;
        }
    }

    static {
        CHECK_STAIR = new MaterialCheck() {

            @Override
            public boolean checkMaterial(Material material) {
                switch(material){
                    case ACACIA_STAIRS:
                    case BRICK_STAIRS:
                    case BIRCH_STAIRS:
                    case COBBLESTONE_STAIRS:
                    case DARK_OAK_STAIRS:
                    case NETHER_BRICK_STAIRS:
                    case QUARTZ_STAIRS:
                    case RED_SANDSTONE_STAIRS:
                    case SANDSTONE_STAIRS:
                    case OAK_STAIRS:
                    case SPRUCE_STAIRS:
                    case JUNGLE_STAIRS:
                    case STONE_BUTTON:
                    case OAK_BUTTON:
                    case IRON_TRAPDOOR:
                    case ENCHANTING_TABLE:
                    case SOUL_SAND:
                    case BREWING_STAND:
                    case END_PORTAL_FRAME:
                    case DRAGON_EGG:
                    case TRIPWIRE_HOOK:
                    case COCOA:
                    case CHEST:
                    case ENDER_CHEST:
                    case TRAPPED_CHEST:
                    case FLOWER_POT:
                    case ANVIL:
                    case COMPARATOR:
                    case DAYLIGHT_DETECTOR:
                    case HOPPER:
                    case SHULKER_BOX:
                    case TURTLE_EGG:
                    case SEA_PICKLE:
                    case OAK_SLAB:
                    case JUNGLE_SLAB:
                    case EXPOSED_CUT_COPPER_SLAB:
                    case WEATHERED_CUT_COPPER_SLAB:
                    case OXIDIZED_CUT_COPPER_SLAB:
                    case WEATHERED_CUT_COPPER_STAIRS:
                    case CUT_COPPER_STAIRS:
                    case CUT_COPPER_SLAB:
                    case EXPOSED_CUT_COPPER_STAIRS:
                    case TWISTING_VINES:
                    case CAVE_VINES:
                    case TWISTING_VINES_PLANT:
                    case WEEPING_VINES:
                    case WEEPING_VINES_PLANT:
                    case CAVE_VINES_PLANT:
                        return true;
                    default:
                        return false;
                }
            }
        };
        CHECK_STEP = new MaterialCheck() {

            @SuppressWarnings("deprecation")
            @Override
            public boolean checkMaterial(Material material) {
                switch(material){
                    case LEGACY_STEP:
                    case LEGACY_WOOD_STEP:
                        return true;
                    default:
                        return false;
                }
            }
        };
    }

}
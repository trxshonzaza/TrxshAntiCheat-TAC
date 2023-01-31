package com.trxsh.anticheat.utils;

import com.trxsh.anticheat.Core;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Objects;

public class PlayerUtility {

    public static boolean isOnGroundProper(Player player) {
        Entity entity = (Entity)player;
        if(entity.isOnGround()) {
            return true;
        }
        return false;
    }

    public static boolean isNearGround(Location location) {
        double expand = 0.3;
        for(double x = -expand; x <= expand; x += expand) {
            for(double z = -expand; z <= expand; z += expand) {
                if(location.clone().add(x, -0.5001, z).getBlock().getType() != Material.AIR) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isNearBlock(Location location) {
        double expand = 0.3;
        for(double x = -expand; x <= expand; x += expand) {
            for(double z = -expand; z <= expand; z += expand) {
                if(location.clone().add(x, location.getY(), z).getBlock().getType() != Material.AIR && !location.clone().add(x, location.getY(), z).getBlock().isLiquid()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isValidUser(Player player)  {
        User user = Objects.requireNonNull(Core.getUser(player));
        if(user == null) {
            return true;
        }
        return false;
    }

    public static boolean isRoughlyEqual(double d1, double d2) {
        return Math.abs(d1 - d2) < 0.001;
    }

    public static boolean Is2BlocksOffGround(Location location) {
        return location.subtract(0, 3, 0).getBlock().getType() == Material.AIR;
    }

    public static boolean isUnderEntity(Player player) {
        for(Entity entity : player.getNearbyEntities(0.1, 0.1, 0.1)) {
            if(entity.getType() == EntityType.BOAT && entity.getLocation().getY() < player.getLocation().getY()) {
                return true;
            }
        }

        return false;
    }
}

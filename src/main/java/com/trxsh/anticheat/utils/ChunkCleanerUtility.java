package com.trxsh.anticheat.utils;

import com.trxsh.anticheat.Core;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Deprecated
public class ChunkCleanerUtility extends BukkitRunnable {

    public void CheckChunks() {

        for(World world : Bukkit.getWorlds()) {
            for(Chunk chunk : world.getLoadedChunks()) {
                for(Entity entity : chunk.getEntities()) {
                    if(entity instanceof Boat) {
                        if(chunk.getEntities().length > 4) {
                            //double eject to make sure player or entity is fully ejected from boat
                            entity.eject();
                            Core.broadcastOperators("boat was removed at pos X: " + entity.getLocation().getX() + ", Z: " + entity.getLocation().getZ() + " at chunk number " + Math.round(chunk.getX() + chunk.getZ()));
                            entity.remove();
                        }
                    }
                }
            }
        }
    }

    public void run() {
        CheckChunks();
    }
}

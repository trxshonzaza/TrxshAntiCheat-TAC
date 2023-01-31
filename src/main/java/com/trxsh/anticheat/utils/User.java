package com.trxsh.anticheat.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import com.trxsh.anticheat.Core;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class User {

    private final Player player;

    public boolean onStairSlab;
    public boolean isBeingWatched = false;

    public float flyThreshold;
    public float lastDeltaY, lastAccel;

    private ArrayList<Long> hits = new ArrayList<>();
    private HashMap<Long, Integer> entities = new HashMap<>();
    private long lastTimeHitsCleaned = 0;
    private long lastTimeEntitiesCleaned = 0;
    public long lastFlying = 0;

    public long lastTime;
    public double balance;

    public int CaughtTimes = 0;
    public int killAuraVerbose = 0;
    public int vl = 0;
    public int scaffoldViolation = 0;
    public int airTicks;

    public int amountMined = 0;
    public long minedTime = 0;

    public boolean wasGoingUp = false;
    public int oldYModifier = 0, ticksUp = 0, oldTicksUp = 0;
    public boolean onSlab = false;
    public boolean nearGround;
    public boolean isGliding;

    public User(Player player) {
        this.player = player;
    }

    public void resetCaughtTimes() {
        CaughtTimes = 0;
    }

    public Player getPlayer() {
        return player;
    }

    public void addHit() {
        hits.add(System.currentTimeMillis());
    }

    public int getHits() {
        long start = System.currentTimeMillis();
        ArrayList<Long> toRemove = new ArrayList<>();
        int result = 0;
        for (long l : hits)
            if (start - l > 1000L)
                toRemove.add(l);
            else
                result++;
        hits.removeAll(toRemove);
        toRemove.clear();
        lastTimeHitsCleaned = start;
        return result;
    }

    public void addEntity(int i) {
        entities.put(System.currentTimeMillis(), i);
    }

    public int getEntities() {
        long start = System.currentTimeMillis();
        ArrayList<Long> toRemove = new ArrayList<>();
        ArrayList<Integer> res = new ArrayList<>();
        int result = 0;
        for (long l : entities.keySet()) {
            int entityId = entities.get(l);
            if (start - l > 1000L)
                toRemove.add(l);
            else if (!res.contains(entityId)) {
                result++;
                res.add(entityId);
            }
        }
        hits.removeAll(toRemove);
        toRemove.clear();
        res.clear();
        lastTimeEntitiesCleaned = start;
        return result;
    }

    public long getLastTimeHitsCleaned() {
        return lastTimeHitsCleaned;
    }

    public long getLastTimeEntitiesCleaned() {
        return lastTimeEntitiesCleaned;
    }

    public UUID getUUID() {
        return this.getPlayer().getUniqueId();
    }

    public boolean IsBedrockPlayer() {
        if(this.getPlayer().getName().contains("BED") |
                this.getPlayer().getName().contains(".")) {
            return true;
        }

        return false;
    }

    public int getVerbose() {
        return vl;
    }

    public void setVerbose(int vb) {
        vl = vb;
    }

    public void resetVerbose() {
        vl = 0;
    }

    public void addVerbose() {
        vl++;

        Bukkit.getScheduler().scheduleSyncDelayedTask(Core.instance, new Runnable() {
            public void run() {
                vl--;
            }
        }, 90L);
    }

    public void resetWatched() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Core.instance, new Runnable() {
            public void run() {
                isBeingWatched = false;
            }
        }, 100L);
    }

    public boolean canBeSpeedflagged() {
        return !player.isInWater() && !player.isSwimming() && !player.isGliding() && !player.isDead() && !player.isBlocking()
                && !player.isSneaking() && !player.isInsideVehicle();
    }

    public void addScaffoldVelocity() {
        scaffoldViolation++;

        Bukkit.getScheduler().scheduleSyncDelayedTask(Core.instance, new Runnable() {
            public void run() {
                scaffoldViolation--;
            }
        }, 90L);
    }

    public int getAmountMined() {
        return amountMined;
    }

    public void setAmountMined(int vb) {
        amountMined = vb;
    }

    public void resetMinedAmount() {
        amountMined = 0;
    }

    public void addMinedAmount() {
        amountMined++;

        Bukkit.getScheduler().scheduleSyncDelayedTask(Core.instance, new Runnable() {
            public void run() {
                amountMined--;
            }
        }, 450L);
    }
}
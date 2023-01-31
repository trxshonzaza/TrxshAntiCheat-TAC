package com.trxsh.anticheat.utils;

import org.bukkit.scheduler.BukkitRunnable;

import com.trxsh.anticheat.Core;

@Deprecated
public class Cleaner extends BukkitRunnable {

    @Override
    public void run() {
        for (User user : Core.USERS.values()) {
            user.getHits();
            user.getEntities();
        }
    }

}
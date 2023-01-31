package com.trxsh.anticheat.utils;

import org.bukkit.scheduler.BukkitRunnable;

public class KickCodeCleaner extends BukkitRunnable {
    public void run() {
        KickCode.addRecycledCodes();
    }
}

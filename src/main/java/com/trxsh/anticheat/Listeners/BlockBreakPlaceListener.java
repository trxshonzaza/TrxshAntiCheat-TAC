package com.trxsh.anticheat.Listeners;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.trxsh.anticheat.Core;
import com.trxsh.anticheat.Block.BlockReach;
import com.trxsh.anticheat.Checks.CheckResult;
import com.trxsh.anticheat.utils.User;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockBreakPlaceListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Location blockLocation = e.getBlock().getLocation();
        Location playerLocation = e.getPlayer().getLocation();

        User u = Core.getUser(e.getPlayer());

        CheckResult result = BlockReach.runChecks(u, blockLocation, playerLocation);

        if(result.failed()) {
            Core.log(u, result);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Location blockLocation = e.getBlock().getLocation();
        Location playerLocation = e.getPlayer().getLocation();

        User u = Core.getUser(e.getPlayer());

        CheckResult result = BlockReach.runChecks(u, blockLocation, playerLocation);

        if(result.failed()) {
            Core.log(u, result);
        }
    }

}

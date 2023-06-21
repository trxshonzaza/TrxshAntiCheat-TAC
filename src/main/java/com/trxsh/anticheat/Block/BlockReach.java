package com.trxsh.anticheat.Block;

import com.trxsh.anticheat.Checks.PacketCheck;
import org.bukkit.Location;

import com.trxsh.anticheat.Checks.CheckResult;
import com.trxsh.anticheat.Checks.CheckType;
import com.trxsh.anticheat.utils.Prefix;
import com.trxsh.anticheat.utils.Settings;
import com.trxsh.anticheat.utils.User;
import org.bukkit.event.player.PlayerMoveEvent;

public class BlockReach {

    public static CheckResult runChecks(User u, Location block, Location player) {

        final double distance = player.distance(block);

        if(distance > Settings.MAX_BLOCK_BREAK_DIST) {
            return new CheckResult(true, CheckType.BLOCKREACH, "player broke/placed block too far away, distance = (" + distance + ")");
        }

        return Prefix.getPass(CheckType.BLOCKREACH);
    }
}

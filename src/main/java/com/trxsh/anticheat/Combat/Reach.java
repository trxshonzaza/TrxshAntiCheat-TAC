package com.trxsh.anticheat.Combat;

import com.trxsh.anticheat.Checks.PacketCheck;
import com.trxsh.anticheat.Checks.PacketCombatCheck;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;

import com.trxsh.anticheat.Checks.CheckResult;
import com.trxsh.anticheat.Checks.CheckType;
import com.trxsh.anticheat.utils.Distance;
import com.trxsh.anticheat.utils.Prefix;
import com.trxsh.anticheat.utils.Settings;
import com.trxsh.anticheat.utils.User;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Reach extends PacketCombatCheck {

    public CheckResult runPacketCombatCheck(User user, EntityDamageByEntityEvent event) {
        Distance distance = new Distance(user.getPlayer().getLocation(), event.getEntity().getLocation());
        double x = distance.getXDifference();
        double z = distance.getZDifference();

        double dist = Math.abs(x + z);

        if (dist > Settings.COMBAT_MAX_REACH_SURVIVAL)
            return new CheckResult(true, CheckType.REACH, (x > z ? z > Settings.COMBAT_MAX_REACH_SURVIVAL ? "both are " : x + " is " : z + " is ") + "greather than " + Settings.COMBAT_MAX_REACH_SURVIVAL);

        return Prefix.getPass(CheckType.REACH);
    }

}
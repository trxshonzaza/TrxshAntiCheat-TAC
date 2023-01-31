package com.trxsh.anticheat.Combat;

import com.trxsh.anticheat.Checks.PacketCheck;
import com.trxsh.anticheat.Checks.PacketCombatCheck;
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

        double max = user.getPlayer().getGameMode() == GameMode.CREATIVE ? Settings.COMBAT_MAX_REACH_CREATIVE : Settings.COMBAT_MAX_REACH_SURVIVAL;

        if (x > max || z > max)
            return new CheckResult(true, CheckType.REACH, (x > z ? z > max ? "both are " : x + " is " : z + " is ") + "greather than " + max);

        return Prefix.getPass(CheckType.REACH);
    }

}
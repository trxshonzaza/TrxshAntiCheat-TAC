package com.trxsh.anticheat.Movement;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;

import com.trxsh.anticheat.Checks.CheckResult;
import com.trxsh.anticheat.Checks.CheckType;
import com.trxsh.anticheat.Checks.MoveCheck;
import com.trxsh.anticheat.utils.CancelType;
import com.trxsh.anticheat.utils.Distance;
import com.trxsh.anticheat.utils.Prefix;
import com.trxsh.anticheat.utils.User;

public class InvMove extends MoveCheck {


    public static ArrayList<UUID> OpenInventories = new ArrayList<UUID>();

    public InvMove() {
        super(CheckType.INVMOVE);
        cancelType = CancelType.NOTHING;
    }

    public CheckResult runCheck(User user, Distance distance) {
        final double speedCheck = (distance.getXDifference() > distance.getZDifference() ? distance.getXDifference() : distance.getZDifference());

        if(OpenInventories.contains(user.getPlayer().getUniqueId())) {
            if(speedCheck != 0 && ValidVelocity(speedCheck, user)) {
                user.getPlayer().teleport(distance.getFrom());
                return new CheckResult(true, CheckType.INVMOVE, "player moved while inventory open");
            }
        }

        return Prefix.getPass(CheckType.INVMOVE);
    }

    public boolean ValidVelocity(final double speed, User user) {
        Location locationOnTopOfPlayer = user.getPlayer().getLocation().subtract(0, 1, 0);
        Material material = locationOnTopOfPlayer.getBlock().getType();

        if(speed < 0.32) {
            if(material != Material.WATER & material != Material.LAVA) {
                return false;
            }
        }

        return true;
    }
}

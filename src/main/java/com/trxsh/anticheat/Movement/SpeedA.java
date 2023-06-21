package com.trxsh.anticheat.Movement;

import com.trxsh.anticheat.Checks.CheckResult;
import com.trxsh.anticheat.Checks.CheckType;
import com.trxsh.anticheat.Checks.PacketCheck;
import com.trxsh.anticheat.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerMoveEvent;

public class SpeedA extends PacketCheck {

    public int vl = 0;

    @Override
    public CheckResult runPacketCheck(User user, PlayerMoveEvent event) {

        Distance distance = new Distance(event);

        double speedCheck = (distance.getXDifference() > distance.getZDifference() ? distance.getXDifference() : distance.getZDifference());

        if(MovementUtil.isOnGround(user.getPlayer().getLocation())) {
            if(speedCheck > Settings.MAX_XZ_SPEED) {
                user.getPlayer().teleport(distance.getFrom());
                return new CheckResult(true, CheckType.SPEED, "player moving over usual player speed, player speed = (" + speedCheck + ")");
            }
        }

        return Prefix.getPass(CheckType.SPEED);
    }
}

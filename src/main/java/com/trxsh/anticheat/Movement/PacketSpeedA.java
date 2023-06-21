package com.trxsh.anticheat.Movement;

import com.trxsh.anticheat.Checks.CheckResult;
import com.trxsh.anticheat.Checks.CheckType;
import com.trxsh.anticheat.Checks.PacketCheck;
import com.trxsh.anticheat.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerMoveEvent;

public class PacketSpeedA extends PacketCheck {

    private double lastDist;
    private boolean lastOnGround;
    public int vl = 0;

    public CheckResult runPacketCheck(User user, PlayerMoveEvent event) {
        Distance distance = new Distance(event);
        double distX = event.getTo().getX() - event.getFrom().getX();
        double distZ = event.getTo().getZ() - event.getFrom().getZ();
        double dist = (distX * distX) + (distZ * distZ);
        double lastDist = this.lastDist;
        this.lastDist = dist;

        double speedCheck = (distance.getXDifference() > distance.getZDifference() ? distance.getXDifference() : distance.getZDifference());

        boolean onGround = PlayerUtility.isOnGroundProper(event.getPlayer());
        boolean lastOnGround = this.lastOnGround;
        this.lastOnGround = onGround;

        float friction = 0.91F;
        double shiftedLastDist = lastDist * friction;
        double equalness = dist - shiftedLastDist;
        double scaledEqualness = equalness * 138;

        if(!onGround && !lastOnGround) {
            if(scaledEqualness >= 1.0) {
                if(user.canBeSpeedflagged() && !user.isGliding && !user.getPlayer().isGliding() && speedCheck > Settings.MAX_XZ_SPEED)  {
                    return new CheckResult(true, CheckType.PACKETSPEED, "player went faster than usual and sending too many movement packets");
                }
            }
        }

        return Prefix.getPass(CheckType.PACKETSPEED);
    }

}

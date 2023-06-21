package com.trxsh.anticheat.Movement;

import com.trxsh.anticheat.Checks.CheckResult;
import com.trxsh.anticheat.Checks.CheckType;
import com.trxsh.anticheat.Checks.PacketCheck;
import com.trxsh.anticheat.utils.PlayerUtility;
import com.trxsh.anticheat.utils.Prefix;
import com.trxsh.anticheat.utils.User;
import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerMoveEvent;

@Deprecated
public class FlightB extends PacketCheck {

    private boolean lastOnGround;
    private boolean lastLastOnGround;
    private double lastDistY;

    public CheckResult runPacketCheck(User user, PlayerMoveEvent event) {
        double distY = event.getTo().getY() - event.getFrom().getY();
        double lastDistY = this.lastDistY;
        this.lastDistY = distY;

        double predictedDist = (lastDistY - 0.08D) * 0.9800000190734863D;

        boolean isOnGround = PlayerUtility.isNearGround(event.getTo());

        boolean lastOnGround = this.lastOnGround;
        this.lastOnGround = isOnGround;

        if(!isOnGround && !lastOnGround && !lastLastOnGround && Math.abs(predictedDist) >= 0.020D) {
            if(!PlayerUtility.isRoughlyEqual(distY, predictedDist) && !PlayerUtility.Is2BlocksOffGround(user.getPlayer().getLocation()) && !user.getPlayer().isInsideVehicle() && !PlayerUtility.isUnderEntity(user.getPlayer()) && !user.isGliding && !user.getPlayer().isGliding()) {
                return new CheckResult(true, CheckType.FLIGHT, "predicted value not equal to fly value");
            }
        }

        return Prefix.getPass(CheckType.FLIGHT);
    }

}

package com.trxsh.anticheat.Movement;

import com.trxsh.anticheat.Checks.CheckResult;
import com.trxsh.anticheat.Checks.CheckType;
import com.trxsh.anticheat.Checks.PacketCheck;
import com.trxsh.anticheat.utils.Prefix;
import com.trxsh.anticheat.utils.User;
import org.bukkit.event.player.PlayerMoveEvent;

public class FlightA extends PacketCheck {

    public CheckResult runPacketCheck(User user, PlayerMoveEvent event) {
        if(user == null || user.getPlayer().isInWater() || user.getPlayer().isSwimming())
            return Prefix.getPass(CheckType.FLIGHT);

        float deltaY = (float) (event.getTo().getY() - event.getFrom().getY());

        if(user.airTicks > 2 && !user.onStairSlab && !user.nearGround && deltaY > user.lastDeltaY) {
            if(user.flyThreshold++ > 2) {
                return new CheckResult(true, CheckType.FLIGHT, "player threshold too high");
            }
        } else user.flyThreshold-= user.flyThreshold > 0 ? 0.1f : 0;

        float accel = (deltaY - user.lastDeltaY);

        if(user.airTicks > 1 && Math.abs(accel) < 0.01) {
            if(user.flyThreshold++ > 3) {
                return new CheckResult(true, CheckType.FLIGHT, "player threshold too high");
            }
        } else user.flyThreshold-= user.flyThreshold > 0 ? 0.25f : 0;
        user.lastAccel = accel;
        user.lastDeltaY = deltaY;

        return Prefix.getPass(CheckType.FLIGHT);
    }
}

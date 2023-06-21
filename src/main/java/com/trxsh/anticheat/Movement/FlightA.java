package com.trxsh.anticheat.Movement;

import com.trxsh.anticheat.Checks.CheckResult;
import com.trxsh.anticheat.Checks.CheckType;
import com.trxsh.anticheat.Checks.PacketCheck;
import com.trxsh.anticheat.utils.MovementUtil;
import com.trxsh.anticheat.utils.Prefix;
import com.trxsh.anticheat.utils.User;
import org.bukkit.event.player.PlayerMoveEvent;

public class FlightA extends PacketCheck {

    public CheckResult runPacketCheck(User user, PlayerMoveEvent event) {
        if(user == null || user.getPlayer().isInWater() || user.getPlayer().isSwimming() || MovementUtil.shouldNotFlag(user.getPlayer().getLocation()) || user.getPlayer().isGliding())
            return Prefix.getPass(CheckType.FLIGHT);

        float deltaY = (float) (event.getTo().getY() - event.getFrom().getY());

        if(deltaY > user.lastDeltaY) {
            if(user.flyThreshold++ > 5) {
                return new CheckResult(true, CheckType.FLIGHT, "player threshold too high");
            }
        } else user.flyThreshold-= user.flyThreshold > 0 ? 0.1f : 0;

        float accel = (deltaY - user.lastDeltaY);

        if(Math.abs(accel) < 0.02) {
            if(user.flyThreshold++ > 5) {
                return new CheckResult(true, CheckType.FLIGHT, "accel = " + "(" + accel + ")");
            }
        } else user.flyThreshold-= user.flyThreshold > 0 ? 0.25f : 0;
        user.lastAccel = accel;
        user.lastDeltaY = deltaY;

        return Prefix.getPass(CheckType.FLIGHT);
    }
}

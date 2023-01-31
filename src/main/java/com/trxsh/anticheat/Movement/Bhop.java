package com.trxsh.anticheat.Movement;

import com.trxsh.anticheat.Checks.CheckResult;
import com.trxsh.anticheat.Checks.CheckType;
import com.trxsh.anticheat.Checks.MoveCheck;
import com.trxsh.anticheat.utils.CancelType;
import com.trxsh.anticheat.utils.Distance;
import com.trxsh.anticheat.utils.Prefix;
import com.trxsh.anticheat.utils.Settings;
import com.trxsh.anticheat.utils.User;

public class Bhop extends MoveCheck {

    public Bhop() {
        super(CheckType.BHOP);
        cancelType = CancelType.NOTHING;
    }

    public CheckResult runCheck(User user, Distance distance) {
        final double xzDist = (distance.getXDifference() > distance.getZDifference() ? distance.getXDifference() : distance.getZDifference());


        if(user.getPlayer().isSneaking()) {
            if(!user.wasGoingUp) {
                if(xzDist > Settings.MAX_XZ_SPEED) {
                    user.getPlayer().teleport(distance.getFrom());
                    return new CheckResult(true, CheckType.BHOP, "player bhopping, over usual player speed, player speed = (" + xzDist + ")");
                }
            }
        }

        return Prefix.getPass(CheckType.BHOP);
    }

}

package com.trxsh.anticheat.Movement;

import com.trxsh.anticheat.Checks.CheckResult;
import com.trxsh.anticheat.Checks.CheckType;
import com.trxsh.anticheat.Checks.MoveCheck;
import com.trxsh.anticheat.utils.*;

public class Bhop extends MoveCheck {

    public int vl = 0;

    public Bhop() {
        super(CheckType.BHOP);
        cancelType = CancelType.NOTHING;
    }

    public CheckResult runCheck(User user, Distance distance) {
        final double xzDist = (distance.getXDifference() > distance.getZDifference() ? distance.getXDifference() : distance.getZDifference());


        if(!user.getPlayer().isFlying() && !user.getPlayer().isSwimming()) {
            if(xzDist > Settings.MAX_XZ_SPEED) {
                user.getPlayer().teleport(distance.getFrom());
                return new CheckResult(true, CheckType.BHOP, "player bhopping, over usual player speed, player speed = (" + xzDist + ")");
            }
        }

        return Prefix.getPass(CheckType.BHOP);
    }

}

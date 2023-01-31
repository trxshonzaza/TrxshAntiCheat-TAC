package com.trxsh.anticheat.Movement;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import com.trxsh.anticheat.Checks.CheckResult;
import com.trxsh.anticheat.Checks.CheckType;
import com.trxsh.anticheat.Checks.MoveCheck;
import com.trxsh.anticheat.utils.CancelType;
import com.trxsh.anticheat.utils.Distance;
import com.trxsh.anticheat.utils.Prefix;
import com.trxsh.anticheat.utils.Settings;
import com.trxsh.anticheat.utils.User;

public class NoSlowDown extends MoveCheck {

    public NoSlowDown() {
        super(CheckType.NOSLOWDOWN);
        cancelType = CancelType.NOTHING;
    }

    public CheckResult runCheck(User user, Distance distance) {
        final double xzDist = (distance.getXDifference() > distance.getZDifference() ? distance.getXDifference() : distance.getZDifference());

        if(user.getPlayer().isBlocking()) {
            if(xzDist > Settings.MAX_XZ_BLOCKING_SPEED) {
                if(!user.wasGoingUp) {
                    user.getPlayer().teleport(distance.getFrom());
                    return new CheckResult(true, CheckType.NOSLOWDOWN, "player moved too fast while blocking, player speed = (" + xzDist + "), max = (" + Settings.MAX_XZ_BLOCKING_SPEED + ")");
                }
            }
        }

        //returns null causes exception if player has no boots
        try {
            Material blockMat = user.getPlayer().getLocation().getBlock().getType();

            for(Material mat : Settings.SLOW_DOWN_BLOCKS) {
                if(blockMat == mat) {
                    if(xzDist > Settings.MAX_XZ_SPEED) {
                        if(!user.wasGoingUp) {
                            if(user.getPlayer().getInventory().getBoots() != null) {
                                if(!user.getPlayer().getInventory().getBoots().containsEnchantment(Enchantment.SOUL_SPEED)) {
                                    user.getPlayer().teleport(distance.getFrom());
                                    return new CheckResult(true, CheckType.NOSLOWDOWN, "player too fast on slow down block, speed = (" + xzDist + ")");
                                }
                            } else {
                                user.getPlayer().teleport(distance.getFrom());
                                return new CheckResult(true, CheckType.NOSLOWDOWN, "player too fast on slow down block, speed = (" + xzDist + ")");
                            }
                        }
                    }
                }
            }
        } catch(NullPointerException e) {

        }



        return Prefix.getPass(CheckType.NORMALMOVEMENTS);
    }

}

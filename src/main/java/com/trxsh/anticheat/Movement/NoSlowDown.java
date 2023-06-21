package com.trxsh.anticheat.Movement;

import com.trxsh.anticheat.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import com.trxsh.anticheat.Checks.CheckResult;
import com.trxsh.anticheat.Checks.CheckType;
import com.trxsh.anticheat.Checks.MoveCheck;

public class NoSlowDown extends MoveCheck {

    public NoSlowDown() {
        super(CheckType.NOSLOWDOWN);
        cancelType = CancelType.NOTHING;
    }

    public int vl = 0;

    public CheckResult runCheck(User user, Distance distance) {
        final double xzDist = (distance.getXDifference() > distance.getZDifference() ? distance.getXDifference() : distance.getZDifference());

        //TODO: use XZ velocity and ignore Y velocity

        if(user.getPlayer().isBlocking()) {
            if(xzDist > Settings.MAX_XZ_BLOCKING_SPEED) {
                if(MovementUtil.isOnGround(user.getPlayer().getLocation())&& !user.wasGoingUp) {
                    ++vl;

                    if(vl >= 5) {
                        user.getPlayer().teleport(distance.getFrom());
                        return new CheckResult(true, CheckType.NOSLOWDOWN, "player moved too fast while blocking, player speed = (" + xzDist + "), max = (" + Settings.MAX_XZ_BLOCKING_SPEED + ")");
                    }

                } else {
                    vl = 0;
                }
            }
        }

        //returns null causes exception if player has no boots
        try {
            Material blockMat = user.getPlayer().getLocation().getBlock().getType();

            for(Material mat : Settings.SLOW_DOWN_BLOCKS) {
                if(blockMat == mat) {
                    if(xzDist > Settings.MAX_XZ_SLOW_SPEED) {
                        if(!user.wasGoingUp && MovementUtil.isOnGround(user.getPlayer().getLocation())) {
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

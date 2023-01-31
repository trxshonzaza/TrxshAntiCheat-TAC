package com.trxsh.anticheat.Movement;

import com.trxsh.anticheat.Checks.CheckResult;
import com.trxsh.anticheat.Checks.CheckType;
import com.trxsh.anticheat.Checks.PacketCheck;
import com.trxsh.anticheat.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerMoveEvent;

@Deprecated
public class Scaffold extends PacketCheck {

    public boolean lastLastOnGround;
    public boolean lastOnGround;

    public CheckResult runPacketCheck(User user, PlayerMoveEvent event) {
        Entity player = (Entity)event.getPlayer();
        boolean onGround = PlayerUtility.isNearGround(event.getTo());
        boolean lastOnGround = this.lastLastOnGround;
        this.lastOnGround = onGround;

        boolean lastLastOnGround = this.lastOnGround;
        this.lastLastOnGround = lastOnGround;

        if(!onGround && !lastOnGround) {
            user.addScaffoldVelocity();
            if(user.scaffoldViolation > 30) {
                if(player.isOnGround()) {
                    return new CheckResult(true, CheckType.SCAFFOLD, "player sent too many onGround packets, " +
                            "scaffold ususally utilizes no fall");
                }
            }
        }

        return Prefix.getPass(CheckType.SCAFFOLD);
    }
}

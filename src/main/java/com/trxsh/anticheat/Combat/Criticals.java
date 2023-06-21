package com.trxsh.anticheat.Combat;

import com.trxsh.anticheat.Checks.CheckResult;
import com.trxsh.anticheat.Checks.CheckType;
import com.trxsh.anticheat.Checks.PacketCombatCheck;
import com.trxsh.anticheat.utils.Distance;
import com.trxsh.anticheat.utils.Prefix;
import com.trxsh.anticheat.utils.User;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Criticals extends PacketCombatCheck  {

    public static final boolean flagged = false;

    public static CheckResult runChecks(User user, Distance distance) {

        if(flagged) {
            return new CheckResult(true, CheckType.CRITICALS, "player critted entity whilst on ground");
        }

        return Prefix.getPass(CheckType.CRITICALS);
    }

    @Override
    public CheckResult runPacketCombatCheck(User user, EntityDamageByEntityEvent event) {

        if(event.getDamager() instanceof Player) {
            Player p = (Player)event.getDamager();

            if((p.getVelocity().getY() + 0.0784000015258789) <= 0) {
                //this was a critical hit
            }
        }

        if(flagged) {
            return new CheckResult(true, CheckType.CRITICALS, "player critted entity whilst on ground");
        }

        return Prefix.getPass(CheckType.CRITICALS);
    }
}

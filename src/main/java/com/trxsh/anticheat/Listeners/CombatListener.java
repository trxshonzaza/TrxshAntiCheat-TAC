package com.trxsh.anticheat.Listeners;

import com.trxsh.anticheat.Combat.KillAura;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.trxsh.anticheat.Core;
import com.trxsh.anticheat.Checks.CheckResult;
import com.trxsh.anticheat.Checks.CheckType;
import com.trxsh.anticheat.Combat.MultiAura;
import com.trxsh.anticheat.Combat.Reach;
import com.trxsh.anticheat.Combat.WallHit;
import com.trxsh.anticheat.utils.User;

public class CombatListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Player player = (Player) e.getDamager();
            User user = Core.getUser(player);

            if(e.getCause() == DamageCause.THORNS || e.getCause() == DamageCause.BLOCK_EXPLOSION) {
                return;
            }

            if (Core.shouldCheck(user, CheckType.WALLHIT)) {
                CheckResult wallHit = WallHit.runCheck(user, e.getEntity());
                if (wallHit.failed()) {
					e.setCancelled(true); // Remove this line for silent checks
                    Core.log(user, wallHit);
                    return;
                }

            }

            if (Core.shouldCheck(user, CheckType.MULTIAURA)) {
                if(e.getCause() != DamageCause.ENTITY_SWEEP_ATTACK) {
                    CheckResult multiAura = MultiAura.runCheck(user, e.getEntity());
                    if (multiAura.failed()) {
                        e.setCancelled(true); // Remove this line for silent checks
                        Core.log(user, multiAura);
                        return;
                    }
                }
            }
        }
    }
}
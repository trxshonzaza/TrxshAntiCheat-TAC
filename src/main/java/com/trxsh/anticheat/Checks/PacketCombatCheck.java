package com.trxsh.anticheat.Checks;

import com.trxsh.anticheat.utils.User;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public abstract class PacketCombatCheck {

    public abstract CheckResult runPacketCombatCheck(User user, EntityDamageByEntityEvent event);

}

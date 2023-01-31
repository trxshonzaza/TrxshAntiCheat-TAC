package com.trxsh.anticheat.Checks;

import com.trxsh.anticheat.utils.User;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public abstract class PacketCheck {
    public abstract CheckResult runPacketCheck(User user, PlayerMoveEvent event);
}

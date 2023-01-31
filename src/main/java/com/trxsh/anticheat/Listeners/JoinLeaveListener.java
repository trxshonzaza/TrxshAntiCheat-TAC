package com.trxsh.anticheat.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.trxsh.anticheat.Core;
import com.trxsh.anticheat.utils.User;

public class JoinLeaveListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        Core.USERS.put(p.getUniqueId(), new User(p));
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        Core.USERS.remove(p.getUniqueId());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if(e.getEntity() instanceof Player) {
            Player p = e.getEntity();

            Core.USERS.get(p.getUniqueId()).CaughtTimes = 0;
        }
    }
}

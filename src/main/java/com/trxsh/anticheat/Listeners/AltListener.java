package com.trxsh.anticheat.Listeners;

import com.trxsh.anticheat.Core;
import com.trxsh.anticheat.utils.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

@Deprecated
public class AltListener implements Listener {

    HashMap<UUID, String> ips = new HashMap<>();

    @EventHandler
    public void onPlayerJoin(PlayerLoginEvent event) {
        ips.put(event.getPlayer().getUniqueId(), Objects.requireNonNull(event.getPlayer().getAddress()).getHostName().split(":")[0]);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Core.instance, new Runnable() {
            public void run() {
                check();
            }
        }, 500L);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        ips.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerKicked(PlayerKickEvent event) {
        ips.remove(event.getPlayer().getUniqueId());
    }


    public void check() {
        for(UUID id : ips.keySet()) {
            Player player = Bukkit.getPlayer(id);
            String ip = ips.get(id);

            if(ips.containsValue(ip)) {
                if(ip != ips.get(id)) {
                    //TODO: fix this
                }
            }
        }
    }

}

package com.trxsh.anticheat.Listeners;

import com.trxsh.anticheat.Core;
import com.trxsh.anticheat.utils.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;

public class ElytraToggleEvent implements Listener {

    @EventHandler
    public void onToggle(EntityToggleGlideEvent event) {
        if (event.getEntity() instanceof Player) {
            User user = Core.getUser((Player)event.getEntity());
            if(!user.isGliding) {
                user.isGliding = true;
            } else {
                Bukkit.getScheduler().scheduleSyncDelayedTask(Core.instance, new Runnable() {
                    public void run() {
                        user.isGliding = false;
                    }
                }, 200L);
            }
        }
    }

}

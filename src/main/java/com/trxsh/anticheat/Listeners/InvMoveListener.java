package com.trxsh.anticheat.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.trxsh.anticheat.Movement.InvMove;

public class InvMoveListener implements Listener {

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        InvMove.OpenInventories.add(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        InvMove.OpenInventories.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent e) {
        InvMove.OpenInventories.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerConnect(PlayerJoinEvent e) {
        InvMove.OpenInventories.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerKicked(PlayerKickEvent e) {
        InvMove.OpenInventories.remove(e.getPlayer().getUniqueId());
    }
}

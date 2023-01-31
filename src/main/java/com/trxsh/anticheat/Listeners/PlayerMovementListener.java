package com.trxsh.anticheat.Listeners;

import com.trxsh.anticheat.Core;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class PlayerMovementListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        try {
            Core.instance.getFakes()
                    .forEach(npc -> {
                        Location location = npc.getBukkitEntity().getLocation();
                        location.setDirection(event.getPlayer().getLocation().subtract(location).toVector());
                        float yaw = location.getYaw();
                        float pitch = location.getPitch();

                        //rotate head packet - horizontal head movement
                        //move entity packet - vertical head movement
                        ServerGamePacketListenerImpl ps = ((CraftPlayer)event.getPlayer()).getHandle().connection;

                        ps.send(new ClientboundRotateHeadPacket(npc, (byte)((yaw%360)*256/360)));
                        ps.send(new ClientboundMoveEntityPacket.Rot(npc.getBukkitEntity().getEntityId(), (byte)((yaw%360)*256/360), (byte)((pitch%360)*256/360), false));
                    });
           }catch(IllegalArgumentException e) {

        }
    }
}

package com.trxsh.anticheat.Packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.trxsh.anticheat.Checks.CheckResult;
import com.trxsh.anticheat.Checks.CheckType;
import com.trxsh.anticheat.Checks.PacketCheck;
import com.trxsh.anticheat.Core;
import com.trxsh.anticheat.utils.Prefix;
import com.trxsh.anticheat.utils.User;
import org.bukkit.event.player.PlayerMoveEvent;

public class BadPacketsA extends PacketCheck {

    public CheckResult runPacketCheck(User user, PlayerMoveEvent event) {

        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(Core.instance,
                PacketType.Play.Client.POSITION,
                PacketType.Play.Client.POSITION_LOOK) {
            @Override
            public void onPacketReceiving(PacketEvent packetEvent) {
                if(Math.abs(event.getPlayer().getLocation().getPitch()) > 90.0) {
                    Core.log(user, new CheckResult(true, CheckType.BADPACKETS, "player pitch over 90 degree limit"));
                }
            }
        });

        return Prefix.getPass(CheckType.BADPACKETS);
    }

}

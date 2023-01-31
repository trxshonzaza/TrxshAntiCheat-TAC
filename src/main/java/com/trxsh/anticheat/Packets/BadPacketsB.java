package com.trxsh.anticheat.Packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.trxsh.anticheat.Checks.CheckResult;
import com.trxsh.anticheat.Checks.CheckType;
import com.trxsh.anticheat.Checks.PacketCheck;
import com.trxsh.anticheat.Core;
import com.trxsh.anticheat.utils.Prefix;
import com.trxsh.anticheat.utils.User;
import org.bukkit.event.player.PlayerMoveEvent;

public class BadPacketsB extends PacketCheck {

    private boolean wasLastArmAnimation;

    public CheckResult runPacketCheck(User user, PlayerMoveEvent event) {

        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(Core.instance,
                PacketType.Play.Client.USE_ENTITY,
                PacketType.Play.Client.ARM_ANIMATION,
                PacketType.Play.Client.FLYING) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                if(event.getPacketType().equals(PacketType.Play.Client.USE_ENTITY)) {
                    if(!wasLastArmAnimation) {
                        user.vl++;
                        if(user.vl > 20) {
                            Core.log(user, new CheckResult(true, CheckType.BADPACKETS, "player packets sent in bad order and violations over limit"));
                        }
                    }
                }else if(event.getPacketType().equals(PacketType.Play.Client.ARM_ANIMATION)) {
                    wasLastArmAnimation = true;
                }else if(event.getPacketType().equals(PacketType.Play.Client.FLYING)) {
                    wasLastArmAnimation = false;
                }
            }
        });

        return Prefix.getPass(CheckType.BADPACKETS);
    }

}

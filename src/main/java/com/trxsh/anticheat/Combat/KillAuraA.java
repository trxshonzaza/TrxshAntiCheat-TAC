package com.trxsh.anticheat.Combat;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.trxsh.anticheat.Checks.PacketCombatCheck;
import com.trxsh.anticheat.Core;

import com.trxsh.anticheat.Checks.CheckResult;
import com.trxsh.anticheat.Checks.CheckType;
import com.trxsh.anticheat.utils.Prefix;
import com.trxsh.anticheat.utils.User;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class KillAuraA extends PacketCombatCheck {

    public CheckResult runPacketCombatCheck(User user, EntityDamageByEntityEvent e) {
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter
                (Core.instance,
                        PacketType.Play.Client.POSITION,
                        PacketType.Play.Client.LOOK,
                        PacketType.Play.Client.POSITION_LOOK,
                        PacketType.Play.Client.USE_ITEM,
                        PacketType.Play.Client.USE_ENTITY) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                User user = Core.getUser(event.getPlayer());

                if(event.getPacketType().equals(PacketType.Play.Client.USE_ENTITY)) {
                    if(System.currentTimeMillis() - user.lastFlying < 5.5) {

                        if(user.getPlayer().isBlocking())
                            return;

                        user.killAuraVerbose++;

                        if(user.killAuraVerbose > 5) {
                            Core.log(user, new CheckResult(true, CheckType.KILLAURA, "player sent flying packet too late"));
                        }
                    }
                } else {
                    if(user != null) {
                        user.killAuraVerbose = 0;
                        user.lastFlying = System.currentTimeMillis();
                    }
                }
            }
        });
        return Prefix.getPass(CheckType.KILLAURA);
    }

}

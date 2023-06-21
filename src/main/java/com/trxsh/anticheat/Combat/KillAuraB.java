package com.trxsh.anticheat.Combat;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.trxsh.anticheat.Checks.CheckResult;
import com.trxsh.anticheat.Checks.CheckType;
import com.trxsh.anticheat.Checks.PacketCombatCheck;
import com.trxsh.anticheat.Core;
import com.trxsh.anticheat.utils.Prefix;
import com.trxsh.anticheat.utils.User;
import org.bukkit.Bukkit;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class KillAuraB extends PacketCombatCheck {


    @Override
    public CheckResult runPacketCombatCheck(User user, EntityDamageByEntityEvent event) {

        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter
                (Core.instance,
                        PacketType.Play.Client.ARM_ANIMATION,
                        PacketType.Play.Client.USE_ENTITY) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                User user = Core.getUser(event.getPlayer());
                if(event.getPacketType().equals(PacketType.Play.Client.ARM_ANIMATION)) {
                    if(user.getPlayer().isBlocking()) {
                        Core.log(user, new CheckResult(true, CheckType.KILLAURA, "arm animation played whilst blocking"));
                    }
                }
            }
        });

        return Prefix.getPass(CheckType.KILLAURA);
    }
}

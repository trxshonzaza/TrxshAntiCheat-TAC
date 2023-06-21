package com.trxsh.anticheat.Checks;

import com.trxsh.anticheat.Combat.KillAuraA;
import com.trxsh.anticheat.Combat.KillAuraB;
import com.trxsh.anticheat.Combat.Reach;
import com.trxsh.anticheat.Core;
import com.trxsh.anticheat.Movement.*;
import com.trxsh.anticheat.Packets.BadPacketsA;
import com.trxsh.anticheat.utils.FakePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;

public class PacketCheckManager implements Listener {

    public List<PacketCheck> MoveChecks = new ArrayList<>();
    public List<PacketCombatCheck> CombatChecks = new ArrayList<>();

    public PacketCheckManager() {
        //addCheck(new Timer());
        addCheck(new FlightA());
        //addCheck(new FlightB());
        addCheck(new PacketSpeedA());
        addCheck(new SpeedA());
        //addCheck(new Scaffold());
        addCheck(new BadPacketsA());
        //addCheck(new BadPacketsB());
        addCombatCheck(new KillAuraA());
        addCombatCheck(new KillAuraB());
        //addCombatCheck(new Criticals());
        addCombatCheck(new Reach());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if(!event.getPlayer().isOp()) {
            for(PacketCheck check : MoveChecks) {
                CheckResult result = check.runPacketCheck(Core.getUser(event.getPlayer()), event);
                if(result.failed()) {
                    if(!Core.getUser(event.getPlayer()).isBeingWatched) {
                        FakePlayer.spawnFakePlayer(event.getPlayer(), Core.RandomLocation(event.getPlayer()));
                        Core.getUser(event.getPlayer()).isBeingWatched = true;
                        Core.getUser(event.getPlayer()).resetWatched();
                    }

                    event.getPlayer().teleport(event.getFrom());
                    Core.log(Core.getUser(event.getPlayer()), result);
                }
            }
        }
    }

    @EventHandler
    public void onCombat(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player) {
            Player player = (Player)event.getDamager();
            if(!player.isOp()) {
                for(PacketCombatCheck check : CombatChecks) {
                    CheckResult result = check.runPacketCombatCheck(Core.getUser(player), event);
                    if(result.failed()) {

                        if(!Core.getUser(player).isBeingWatched) {
                            FakePlayer.spawnFakePlayer(player, Core.RandomLocation(player));
                            Core.getUser(player).isBeingWatched = true;
                            Core.getUser(player).resetWatched();
                        }

                        Core.log(Core.getUser(player), result);
                    }
                }
            }
        }
    }

    public void addCheck(PacketCheck check) {
        MoveChecks.add(check);
    }

    public void addCombatCheck(PacketCombatCheck check) {
        CombatChecks.add(check);
    }
}

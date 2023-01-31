package com.trxsh.anticheat.Checks;

import java.util.ArrayList;

import com.trxsh.anticheat.Movement.*;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.trxsh.anticheat.Core;
import com.trxsh.anticheat.utils.Distance;
import com.trxsh.anticheat.utils.User;

public class CheckManager implements Listener {

    private ArrayList<MoveCheck> moveChecks;

    public CheckManager() {
        moveChecks = new ArrayList<>();
        String splitter = "+==============+===============+";
        Bukkit.getLogger().info(splitter);
        Bukkit.getLogger().info("\t\tThank You For Downloading TAC.");
        Bukkit.getLogger().info(splitter);
        Bukkit.getLogger().info("\tVersion: " + Core.getPlugin(Core.class).getDescription().getVersion());
        Bukkit.getLogger().info("\tAuthor: WAC For Base, Heavily Modified By Trxsh");
        Bukkit.getLogger().info(splitter);
        Bukkit.getLogger().info(" ");
        Bukkit.getLogger().info(splitter);
        Bukkit.getLogger().info("\t   Check Manager");
        Bukkit.getLogger().info(splitter);
        Bukkit.getLogger().info("\tMovement Checks:");
        addCheck(new NoSlowDown());
        addCheck(new InvMove());
        addCheck(new Bhop());
        // And all other checks
        Bukkit.getLogger().info(splitter);
    }

    private void addCheck(MoveCheck moveCheck) {
        moveChecks.add(moveCheck);
        Bukkit.getLogger().info("\t" + moveCheck.getType().getName() + " has been enabled.");
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        User user = Core.getUser(e.getPlayer());
        Distance distance = new Distance(e);
        for (MoveCheck check : moveChecks)
            if (Core.shouldCheck(user, check.getType())) {
                CheckResult result = check.runCheck(user, distance);
                if (result.failed()) {
                    Core.log(user, result);
                    switch (check.getCancelType()) {
                        case EVENT:
                            e.setTo(e.getFrom());
                            break;
                        case PULLDOWN:
                            //idk what to add here lol
                            break;
                        default:
                            break;
                    }
                }
            }
    }

}
package com.trxsh.anticheat;

import java.util.*;

import com.trxsh.anticheat.Checks.PacketCheckManager;
import com.trxsh.anticheat.Listeners.*;
import com.trxsh.anticheat.utils.*;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.trxsh.anticheat.Checks.CheckManager;
import com.trxsh.anticheat.Checks.CheckResult;
import com.trxsh.anticheat.Checks.CheckType;
import org.checkerframework.checker.nullness.qual.NonNull;

public class Core extends JavaPlugin {

    public static final HashMap<UUID, User> USERS = new HashMap<>();
    public static final HashMap<UUID, KickCode> KICKS = new HashMap<>();
    public static final ArrayList<CheckType> DISABLED_CHECKS = new ArrayList<>();
    public static final ArrayList<String> KICK_LOG = new ArrayList<>();
    public static final ArrayList<Player> BYPASS = new ArrayList<>();
    public static final List<ServerPlayer> FAKE_PLAYER_LIST = new ArrayList<>();

    public static boolean Alert = true;
    public static boolean Logger = true;

    public static Integer CaughtTimeLimit = 20;

    public static Core instance;

    @Override
    public void onEnable() {
        instance = this;
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new JoinLeaveListener(), this);
        pm.registerEvents(new CheckManager(), this);
        pm.registerEvents(new CombatListener(), this);
        pm.registerEvents(new InvMoveListener(), this);
        pm.registerEvents(new BlockBreakListener(), this);
        pm.registerEvents(new PacketCheckManager(), this);
        pm.registerEvents(new PlayerMovementListener(), this);
        pm.registerEvents(new ElytraToggleEvent(), this);

        new KickCodeCleaner().runTaskTimerAsynchronously(this, 5000, 5000);

        if(KickCode.fileExists()) {
            KickCode.load();
        }

        for (Player p : Bukkit.getOnlinePlayers())
            Core.USERS.put(p.getUniqueId(), new User(p));
    }

    @Override
    public void onDisable() {
        KickCode.save();
    }

    /*
    logs player and adds verbose level
     */
    public static void log(User u, CheckResult result) {
        if(u.getPlayer().isOp() || BYPASS.contains(u.getPlayer())) {
            return;
        }

        if (DISABLED_CHECKS.contains(result.getType()))
            throw new IllegalStateException("Error! Tried to log a disabled check!");
        String message = Prefix.getPrefix() + ChatColor.AQUA + u.getPlayer().getName() + ChatColor.GRAY + " " + Prefix.RandomAlert() + " " + ChatColor.RED + result.getType().getName() + ", (x" + u.CaughtTimes + ") " + result.getMessage() + ", VL = " + u.vl;

        Bukkit.getLogger().info(message);
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(Alert) {
                if(p.isOp()) {
                    p.sendMessage(message);
                }
            }
        }

        u.CaughtTimes++;
        String helpMessage = ChatColor.AQUA + "\n" + "false kick? please join discord for support: https://discord.gg/nmPZJQtVE4 ";
        if(u.CaughtTimes > CaughtTimeLimit) {
            Date date = new Date();

            KickCode kickcode = new KickCode(new Random(), u.getPlayer());
            String kickMessage = Prefix.getPrefix().replace(":", "") + ChatColor.RED + "\n" + "Kicked For Suspicious Client Behaviour." + "\n" + "KickCode: " + kickcode.generateKickCode() + helpMessage;
            String logMessage = ChatColor.AQUA + u.getPlayer().getName() + ChatColor.GRAY + " was kicked for " + result.getType().getName() + "." + " date: " + date + ". Kick Code: " +  kickcode.getKickCode();

//			for(Bot b : BOTLIST) {
//				b.remove();
//			}

            if(Logger) {
                if(!KICK_LOG.contains(logMessage)) {
                    KICK_LOG.add(Prefix.getPrefix() + logMessage);
                }
            }

            u.getPlayer().sendMessage(kickMessage);
            kickasync(u.getPlayer(), kickMessage);

            u.CaughtTimes = 0;
            u.vl = 0;

            for(Player player : Bukkit.getOnlinePlayers()) {
                if(player.isOp()) {
                    player.sendMessage(Prefix.getPrefix() + ChatColor.AQUA + u.getPlayer().getName() + ChatColor.GRAY + " was kicked for " + result.getType().getName() + ".");
                }
            }
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(Core.instance, new Runnable() {
            public void run() {
                u.CaughtTimes--;
//		    	BOTLIST.remove(bot);
            }
        }, 300L);

    }

    /*
    gets player user by player instance
     */
    @NonNull
    public static User getUser(Player player) {
        for (User user : USERS.values())
            if (user.getPlayer() == player || user.getPlayer().getUniqueId().equals(player.getUniqueId()))
                return user;
        return null;
    }

    /* commands */
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        try {
            if(cmd.getName().equalsIgnoreCase("heuristics")) {

                if(sender.isOp()) {
                    for(Player p : Bukkit.getOnlinePlayers()) {
                        User u = Core.USERS.get(p.getUniqueId());
                    }

                    if(args[0].equalsIgnoreCase("high")) {
                        sender.sendMessage(Prefix.getPrefix() + "Heruistics Mode Has Been Set To 'High'");
                        Core.CaughtTimeLimit = 10;
                        return true;
                    }
                    else if(args[0].equalsIgnoreCase("medium")) {
                        sender.sendMessage(Prefix.getPrefix() + "Heruistics Mode Has Been Set To 'Medium'");
                        Core.CaughtTimeLimit = 20;
                        return true;
                    }
                    else if(args[0].equalsIgnoreCase("low")) {
                        Core.CaughtTimeLimit = 30;
                        sender.sendMessage(Prefix.getPrefix() + "Heruistics Mode Has Been Set To 'Low'");
                        return true;
                    } else {
                        sender.sendMessage(Prefix.getPrefix() + "Usage: /heruistics <low/medium/high>");
                        return true;
                    }
                } else {
                    sender.sendMessage(Prefix.getPrefix() + ChatColor.RED + "you do not have permission to use this command!");
                    return true;
                }
            }

            if(cmd.getName().equalsIgnoreCase("offencelist")) {
                if(sender.isOp()) {
                    Core.OffenceList(sender);
                    return true;
                } else {
                    sender.sendMessage(Prefix.getPrefix() + ChatColor.RED + "you do not have permission to use this command!");
                    return true;
                }
            }

            if(cmd.getName().equalsIgnoreCase("banslist")) {

                if(sender.isOp()) {
                    Core.PrintBans(sender);
                    return true;
                } else {
                    sender.sendMessage(Prefix.getPrefix() + ChatColor.RED + "you do not have permission to use this command!");
                    return true;
                }
            }

            if(cmd.getName().equalsIgnoreCase("alerting")) {

                if(sender.isOp()) {
                    if(Core.Alert) {
                        Core.Alert = false;
                        sender.sendMessage(Prefix.getPrefix() + "alerting is disabled");
                        return true;
                    } else {
                        Core.Alert = true;
                        sender.sendMessage(Prefix.getPrefix() + "alerting is enabled");
                        return true;
                    }
                } else {
                    sender.sendMessage(Prefix.getPrefix() + ChatColor.RED + "you do not have permission to use this command!");
                    return true;
                }
            }

            if(cmd.getName().equalsIgnoreCase("logkicks")) {

                if(sender.isOp()) {
                    if(Core.Logger) {
                        Core.Logger = false;
                        sender.sendMessage(Prefix.getPrefix() + "logging is disabled");
                        return true;
                    } else {
                        Core.Logger = true;
                        sender.sendMessage(Prefix.getPrefix() + "logging is enabled");
                        return true;
                    }
                } else {
                    sender.sendMessage(Prefix.getPrefix() + ChatColor.RED + "you do not have permission to use this command!");
                    return true;
                }
            }

            if(cmd.getName().equalsIgnoreCase("kicklog")) {

                if(sender.isOp()) {
                    Core.PrintLogs(sender);
                    return true;
                } else {
                    sender.sendMessage(Prefix.getPrefix() + ChatColor.RED + "you do not have permission to use this command!");
                    return true;
                }
            }

            if(cmd.getName().equalsIgnoreCase("clearlog")) {

                if(sender.isOp()) {
                    if(Core.KICK_LOG.size() == 0) {
                        sender.sendMessage(Prefix.getPrefix() + ChatColor.RED + "nothing is logged!");
                        return true;
                    } else {
                        Core.KICK_LOG.clear();
                        sender.sendMessage(Prefix.getPrefix() + ChatColor.GREEN + "cleared log successfully!");
                        return true;
                    }
                } else {
                    sender.sendMessage(Prefix.getPrefix() + ChatColor.RED + "you do not have permission to use this command!");
                    return true;
                }
            }

        } catch(IndexOutOfBoundsException e) {
            sender.sendMessage(Prefix.getPrefix() + "Usage: /heruistics <low/medium/high>");
            return true;
        }

        try {
            if(cmd.getName().equalsIgnoreCase("givebypass")) {
                if(sender.isOp()) {
                    if(args[0].length() != 0) {
                        Player p = Bukkit.getPlayer(args[0]);
                        for(Player player : Bukkit.getOnlinePlayers()) {
                            if(p.getName() == player.getName()) {
                                if(Core.BYPASS.contains(p)) {
                                    sender.sendMessage(Prefix.getPrefix() + "Player Already In List!");
                                    return true;
                                } else {
                                    Core.BYPASS.add(p);
                                }
                            } else {

                            }
                        }
                        sender.sendMessage(Prefix.getPrefix() + "player added to list.");
                        return true;
                    }
                } else {
                    sender.sendMessage(Prefix.getPrefix() + ChatColor.RED + "you do not have permission to use this command!");
                    return true;
                }
            }
        }catch(NullPointerException e) {
            sender.sendMessage(Prefix.getPrefix() + "usage: /givebypass <playername>");
            return true;
        }

        try {
            if(cmd.getName().equalsIgnoreCase("revokebypass")) {
                if(sender.isOp()) {
                    if(args[0].length() != 0) {
                        Player p = Bukkit.getPlayer(args[0]);
                        Core.BYPASS.remove(p);
                        sender.sendMessage(Prefix.getPrefix() + "player removed from list.");
                        return true;
                    }
                } else {
                    sender.sendMessage(Prefix.getPrefix() + ChatColor.RED + "you do not have permission to use this command!");
                    return true;
                }
            }
        }catch(NullPointerException e) {
            sender.sendMessage(Prefix.getPrefix() + "usage: /revokebypass <playername>");
            return true;
        }

        if(cmd.getName().equalsIgnoreCase("bypasslist")) {
            if(sender.isOp()) {
                Core.BypassList(sender);
                return true;
            } else {
                sender.sendMessage(Prefix.getPrefix() + ChatColor.RED + "you do not have permission to use this command!");
                return true;
            }
        }
        if(cmd.getName().equalsIgnoreCase("createfakenpc")) {
            if(sender.isOp()) {
                try {
                    FakePlayer.spawnFakePlayer((Player)sender, Core.RandomLocation((Player)sender));
                    return true;
                }catch(Exception e) {
                    sender.sendMessage(Prefix.getPrefix() + "failed to create npc! do you have protocolLib installed?");
                }
            } else {
                sender.sendMessage(Prefix.getPrefix() + ChatColor.RED + "you do not have permission to use this command!");
                return true;
            }
        }

        return false;
    }

    public static boolean shouldCheck(User user, CheckType type) {
        // TODO Add config with disabled checks & disable checks by command
        return !DISABLED_CHECKS.contains(type);
    }

    public static boolean isSilent() {
        return false;
    }

    public static Location RandomLocation(Player sender) {
        List<Location> locs = new ArrayList<>();
        Location playerLoc = sender.getLocation();
        Random rand = new Random();

        locs.add(playerLoc.add(rand.nextInt(1), 1, rand.nextInt(1)));
        locs.add(playerLoc.add(rand.nextInt(2), 1, rand.nextInt(2)));

        return (Location) locs.stream().toArray()[rand.nextInt(locs.size())];
    }

    public static void BypassList(CommandSender sender) {
        sender.sendMessage(Prefix.getPrefix() + "--------------------------------------");
        sender.sendMessage(Prefix.getPrefix() + "Bypass List");

        if(BYPASS.size() == 0) {
            sender.sendMessage(Prefix.getPrefix() + ChatColor.RED + "No Players In List!");
        } else {
            for(Player p : BYPASS) {
                sender.sendMessage(Prefix.getPrefix() + "Player Name: " + p.getName());
            }
        }

        sender.sendMessage(Prefix.getPrefix() + "--------------------------------------");
    }

    public static void PrintLogs(CommandSender sender) {
        sender.sendMessage(Prefix.getPrefix() + "--------------------------------------");
        sender.sendMessage(Prefix.getPrefix() + "Log List");

        if(KICK_LOG.size() == 0) {
            sender.sendMessage(Prefix.getPrefix() + ChatColor.RED + "No Logs Available!");
        } else {
            for(String s : KICK_LOG) {
                sender.sendMessage(s);
            }
        }

        sender.sendMessage(Prefix.getPrefix() + "--------------------------------------");
    }

    public static void PrintBans(CommandSender sender) {
        sender.sendMessage(Prefix.getPrefix() + "--------------------------------------");
        sender.sendMessage(Prefix.getPrefix() + "List Of Banned Players");

        if(Bukkit.getBannedPlayers().size() == 0) {
            sender.sendMessage(Prefix.getPrefix() + ChatColor.RED + "There Are No Bans!");
        } else {
            for(OfflinePlayer player : Bukkit.getBannedPlayers()) {

                sender.sendMessage(Prefix.getPrefix() + "Player Name: " + player.getName());
            }
        }

        sender.sendMessage(Prefix.getPrefix() + "--------------------------------------");
    }

    public static void OffenceList(CommandSender sender) {
        sender.sendMessage(Prefix.getPrefix() + "--------------------------------------");
        sender.sendMessage(Prefix.getPrefix() + "Offence List Of Online Players");
        for(UUID uuid : USERS.keySet()) {
            User user = USERS.get(uuid);

            sender.sendMessage(Prefix.getPrefix() + ChatColor.AQUA + user.getPlayer().getName() + ChatColor.GRAY + ", offences: " + user.CaughtTimes);
        }
        sender.sendMessage(Prefix.getPrefix() + "--------------------------------------");
    }

    public static void kickasync(Player player, String message) {
        Bukkit.getScheduler().runTask(Core.instance, new Runnable() {
            public void run() {
                player.kickPlayer(message);
            }
        });
    }

    public List<ServerPlayer> getFakes() {
        return FAKE_PLAYER_LIST;
    }

    public static void broadcastOperators(String str) {
        for(Player player : Bukkit.getOnlinePlayers()) {
            if(player.isOp()) {
                player.sendMessage(Prefix.getPrefix() + str);
            }
        }
    }
}

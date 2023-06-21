package com.trxsh.anticheat.utils;

import java.util.Random;

import org.bukkit.ChatColor;

import com.trxsh.anticheat.Checks.CheckResult;
import com.trxsh.anticheat.Checks.CheckType;

public class Prefix {

    public static String prefix = ChatColor.DARK_PURPLE + "TrxshAntiCheat" + ": " + ChatColor.GRAY;
    public static String[] alertMessages = new String[] { "Caught In 4K Using", "Cringed", "Caught Lacking Using", "Very Sus, Using" };

    public static String getPrefix() {
        return prefix;
    }

    public static CheckResult getPass(CheckType type) {
        return new CheckResult(false, type, "");
    }

    public static String RandomAlert() {
        Random random = new Random();

        return alertMessages[random.nextInt(alertMessages.length)];
    }

    public static void Fail() {

    }
}

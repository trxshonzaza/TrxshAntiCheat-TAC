package com.trxsh.anticheat.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

@SuppressWarnings("deprecation")
public class Settings {

    public static final double COMBAT_MAX_REACH_SURVIVAL = 5.42; // You can play around and improve this...
    public static final double COMBAT_MAX_REACH_CREATIVE = 5.4; // You can play around and improve this...
    public static final int MAX_ENTITIES = 8;

    public static final int ROUNDING_PLACES = 6;

    public static final double MAX_XZ_BLOCKING_SPEED = 0.215;
    public static double MAX_XZ_SPEED = 0.25;
    public static final double MAX_BLOCK_BREAK_DIST = 5.95;

    public static List<Material> SLOW_DOWN_BLOCKS;
    static {
        SLOW_DOWN_BLOCKS = new ArrayList<Material>();

        SLOW_DOWN_BLOCKS.add(Material.SOUL_SAND);
        SLOW_DOWN_BLOCKS.add(Material.COBWEB);
        SLOW_DOWN_BLOCKS.add(Material.HONEY_BLOCK);
        SLOW_DOWN_BLOCKS.add(Material.SLIME_BLOCK);
    }

    public static double round(double value, int places) {
        if (places < 0)
            throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static double round(double value) {
        return round(value, ROUNDING_PLACES);
    }

}
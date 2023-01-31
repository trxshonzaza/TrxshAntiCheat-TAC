package com.trxsh.anticheat.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Stream;

public class KickCode {

    public Random random;
    public int kickCode;
    public Player player;
    private static File kickCodes = new File("TAC-KickCodes.cds");
    private static String newLine = System.getProperty("line.separator");

    private static final List<Integer> availableCodes;
    static {
        availableCodes = new ArrayList<>();

        for(int i = 0; i < 32767; i++) {
            availableCodes.add(i);
        }
    }

    public static final HashMap<Integer, UUID> usedCodes = new HashMap<>();

    private static final List<Integer> recycledCodes = new ArrayList<>();

    public KickCode(Random r, Player p) {
        random = r;
        player = p;
    }

    public int getKickCode() {
        return kickCode;
    }

    public int generateKickCode() {
        int code = random.nextInt(availableCodes.size());
        availableCodes.remove(code);
        recycledCodes.add(code);
        kickCode = code;
        usedCodes.put(code, player.getUniqueId());
        return code;
    }

    @Deprecated
    private void flushCodes() {
       availableCodes.clear();
    }

    public static void addRecycledCodes() {
        try {
            for(int rec : recycledCodes) {
                availableCodes.add(rec);
                recycledCodes.remove(rec);
            }
        }catch(IndexOutOfBoundsException e) {
            Bukkit.getLogger().warning(Prefix.getPrefix() + "Failed To Add Recycled Code!");
        }
    }

    public static Stream<Integer> recycledStream() {
        return recycledCodes.stream();
    }

    public static Stream<Integer> codeStream() {
        return availableCodes.stream();
    }

    public static void save() {
        try {
            FileWriter writer = new FileWriter(kickCodes);

            for(Integer i : usedCodes.keySet()) {
                UUID id = usedCodes.get(i);

                String toWrite = i + "//" + id.toString() + newLine;
                writer.write(toWrite);
            }
            writer.flush();
            writer.close();
        }catch(IOException e) {

        }
    }

    public static void load() {
        try {
            usedCodes.clear();
            recycledCodes.clear();
            List<String> lines = Files.readAllLines(kickCodes.toPath());

            for(String line : lines) {
                int code = Integer.parseInt(line.split("//")[0]);
                UUID id = UUID.fromString(line.split("//")[1]);

                usedCodes.put(code, id);
                recycledCodes.add(code);
            }
        }catch(IOException e) {

        }
    }

    public static boolean fileExists() {
        return kickCodes.exists();
    }
}

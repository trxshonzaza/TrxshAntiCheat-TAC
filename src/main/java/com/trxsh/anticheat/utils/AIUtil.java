package com.trxsh.anticheat.utils;

public class AIUtil {

    public static AIUtil instance;
    public int points;

    public static AIUtil getAI() {
        return instance;
    }

    public void reward() {
        points++;
    }

    public void deduct() {
        points--;
    }

    public void analyze() {

    }

}

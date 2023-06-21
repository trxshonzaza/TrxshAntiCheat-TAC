package com.trxsh.anticheat.Checks;

public enum CheckType {

    REACH("Reach"),
    WALLHIT("WallHit"),
    MULTIAURA("MultiAura"),
    NORMALMOVEMENTS("Abnormal Movements"),
    FLIGHT("Flight"),
    NOSLOWDOWN("No Slow"),
    CRITICALS("Criticals"),
    INVMOVE("Inventory Move"),
    BHOP("Bhop"),
    BLOCKREACH("Block Reach"),
    KILLAURA("KillAura"),
    PACKETSPEED("Packet Speed"),
    BADPACKETS("Bad Packets"),
    SCAFFOLD("Scaffold"),
    SPEED("Speed");

    private String string;

    private CheckType(String string) {
        this.string = string;
    }

    public String getName() {
        return string;
    }

}
package com.trxsh.anticheat.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.datafixers.util.Pair;
import com.trxsh.anticheat.Core;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_19_R2.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import net.minecraft.world.entity.EquipmentSlot;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class FakePlayer {

    public static void spawnFakePlayer(Player pl, Location location) {

        CraftPlayer craftPlayer = (CraftPlayer)pl;
        ServerPlayer sp = craftPlayer.getHandle();

        MinecraftServer server = sp.getServer();
        ServerLevel level = sp.getLevel();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), ChatColor.RED + "dY3N572sM");

        String signature = "dY3N572sMGB1GF2rgZmxL0rAMVc94vArDSeQJ8n1xAs4KK5khp1zteh6ye62yQYr0sQagdAPsjrorUJfuJXo5RjpePSySIlYl+fzUGVbeJ4Ftya8U3Vlp80P3SwOX+PEdapKQst5mlZwlfccl/Ly2S4boTrS7C4hy+XPOPOImwX79w1Clf/9B45J8HkctjXsO84CSwloeNjP/kIr9XdA6QAcPEiqNq04RUaY9JmjukLLB7kHORVzbU1wzaLiglxCAKWA8PwSvHS709j3jEMMUzQ/Sn5aVUoIsdFFD9gHZQdewoY1BiMMjXEWiij5PDvIFEp6QEPTOT3fOozkwCVT9n1b+y9sdW/sNM7LuNdT7w2jd32ZxOA9q9Wwi5AZPMxCqgWUbgUVOPF+3WfCC0UHwQAz6leW02ZauFKg1JC9AUlRrjaqFQBsGx3FLuYMGKBzSsbMIK9TTDOQQjeyTEaAteL4LXX8VJvOl65wE/objEvZGsNh9WlTsz1q6UzcO/inIobQoZT9U5WaM+PmDUEvQ8wbYgOeXzCagymUA9Or4Cut8wfozZ41vDo+R+tEkxbOaGxgQDkn4XyANpzj2IBAM3FYqEmCbVUdXpVNqA34zPKiYE0UC45EUiU9l1HRxsRLfYT93gZPXdQDxLBWLq5m6dj3IPrLBDC0NIeS0zwswlM=";
        String tex = "ewogICJ0aW1lc3RhbXAiIDogMTY0NTkzNzQ1MDg2MSwKICAicHJvZmlsZUlkIiA6ICIwNjlhNzlmNDQ0ZTk0NzI2YTViZWZjYTkwZTM4YWFmNSIsCiAgInByb2ZpbGVOYW1lIiA6ICJOb3RjaCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8yOTIwMDlhNDkyNWI1OGYwMmM3N2RhZGMzZWNlZjA3ZWE0Yzc0NzJmNjRlMGZkYzMyY2U1NTIyNDg5MzYyNjgwIgogICAgfQogIH0KfQ==";

        gameProfile.getProperties().put("textures", new Property("textures", tex, signature));

        ServerPlayer npc = new ServerPlayer(server, level, gameProfile);
        npc.setPos(location.getX(), location.getY(), location.getZ());

        ServerGamePacketListenerImpl ps = sp.connection;

        ps.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, npc));
        ps.send(new ClientboundPlayerInfoRemovePacket(List.of(new UUID[]{npc.getUUID()})));
        ps.send(new ClientboundAddPlayerPacket(npc));

        Bukkit.getScheduler().runTaskLater(Core.instance, new Runnable() {
            @Override
            public void run() {
                ps.send(new ClientboundRemoveEntitiesPacket(npc.getId()));
            }
        }, 10L);

        ps.send(new ClientboundAddPlayerPacket(npc));
        Core.instance.getFakes().add(npc);

        ItemStack axe = new ItemStack(Material.DIAMOND_AXE);
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        ps.send(new ClientboundSetEquipmentPacket(npc.getBukkitEntity().getEntityId(), List.of(new Pair<>(EquipmentSlot.MAINHAND, CraftItemStack.asNMSCopy(axe)), new Pair<>(EquipmentSlot.OFFHAND, CraftItemStack.asNMSCopy(sword)))));

        Bukkit.getScheduler().runTaskLater(Core.instance, new Runnable() {
            @Override
            public void run() {
                ps.send(new ClientboundRemoveEntitiesPacket(npc.getBukkitEntity().getEntityId()));
                Core.instance.getFakes().remove(npc);
            }
        }, 100L);
    }

}

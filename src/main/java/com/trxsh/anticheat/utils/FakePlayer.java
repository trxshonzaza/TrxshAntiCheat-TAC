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
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), ChatColor.RED + "");

        String signature = "Nwi3iigzeeZlKethlo+tIlnTq1qetWsd417sa4zkP6jONZ4HKP241ayNoDxxD7uu0NLYbEOw+emFpAHUXpMOamjTl3fl1NyazaqChYXMsnmPq8uhYrituXmZk+MNnmv9G1JvrACOy6rMo93Y+SAFGOeJGeIb5yKkFba3VxLuku5mLaLmV4uUWAQXdgPeZdhvwoK2nSQQP/CyQec1glL+drxEPOoq/aglYQF+aGUCNvMH/4LCAcfpJWysymKtF0lfSgawJfSSLlZfPR/CSR8AISZVRNNAGixeb8HF+AT1c1zS/euiDwUjUgVvwRKCLetzEcpUHXz2STKaIewhE957C9r7fUbLWDXEcsx9A8IL16tWm/pwB3GGGp/U69chlBgzZGj948Xzo2+XJYbewvA8aBLnu+e+I/8sbFCwhRKbsMM8FabfcdP8CVzMFgzKKCmIswPZw+gOrHEh+Z8841dyQUs+pCzMCUt52qoJ+hePHtlIyMEbJfdSj0SfLkloyNYG47a1LMANY+MBxD/FzlgvIgYA5zpr2kMGssD5kY82WLCk5dbb/2z7MijiX/if4JiavuvdcshWztvFDfQCdH35iGSSI/Y+Jf3U6CifNkb5HSw0A+eGOTpnC40ji90Oe/ftEw9hMcPw/DGTqYz7xh6p8ZRvZ/vDFAfh/+rMfUtr8EQ=";
        String tex = "ewogICJ0aW1lc3RhbXAiIDogMTY3NTMwMDE3ODk3NCwKICAicHJvZmlsZUlkIiA6ICIxYmVkNmFjMjk0YWM0MGYxOWMxYmZlZDY3ZTU3NTkwMyIsCiAgInByb2ZpbGVOYW1lIiA6ICJNaW5jZXJzIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzRmNjUxYTE2Mzc5YjdiOGNhYTQ1NzAxN2FjMjQ2MzIzZGM3Zjk0NjJkMWRjMjNjNmRhM2JkNzU4ZWU1OGJlOTIiCiAgICB9CiAgfQp9";

        gameProfile.getProperties().put("textures", new Property("textures", tex, signature));

        ServerPlayer npc = new ServerPlayer(server, level, gameProfile);
        npc.setPos(location.getX(), location.getY(), location.getZ());

        ServerGamePacketListenerImpl ps = sp.connection;

        ps.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, npc));
        ps.send(new ClientboundAddPlayerPacket(npc));
        ps.send(new ClientboundAddEntityPacket(npc));

        Core.instance.getFakes().add(npc);

        ItemStack axe = new ItemStack(Material.NETHERITE_AXE);
        ItemStack helmet = new ItemStack(Material.NETHERITE_HELMET);
        ItemStack chest = new ItemStack(Material.NETHERITE_CHESTPLATE);
        ItemStack leg = new ItemStack(Material.NETHERITE_LEGGINGS);
        ItemStack boot = new ItemStack(Material.NETHERITE_BOOTS);

        ps.send(new ClientboundSetEquipmentPacket(npc.getBukkitEntity().getEntityId(), List.of(new Pair<>(EquipmentSlot.MAINHAND, CraftItemStack.asNMSCopy(axe)), new Pair<>(EquipmentSlot.HEAD, CraftItemStack.asNMSCopy(helmet)), new Pair<>(EquipmentSlot.CHEST, CraftItemStack.asNMSCopy(chest)), new Pair<>(EquipmentSlot.LEGS, CraftItemStack.asNMSCopy(leg)), new Pair<>(EquipmentSlot.FEET, CraftItemStack.asNMSCopy(boot)))));

        Bukkit.getScheduler().runTaskLater(Core.instance, new Runnable() {
            @Override
            public void run() {
                ps.send(new ClientboundRemoveEntitiesPacket(npc.getBukkitEntity().getEntityId()));
                ps.send(new ClientboundPlayerInfoRemovePacket(List.of(new UUID[]{npc.getUUID()})));
                Core.instance.getFakes().remove(npc);
            }
        }, 100L);
    }

}

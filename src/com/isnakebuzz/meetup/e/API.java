package com.isnakebuzz.meetup.e;

import com.comphenix.protocol.ProtocolLib;
import com.isnakebuzz.meetup.a.Main;
import com.isnakebuzz.meetup.b.States;
import com.isnakebuzz.meetup.f.Starting;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import net.minecraft.server.v1_8_R3.BiomeBase;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.permissions.BroadcastPermissions;

public class API {
    
    public static ArrayList<Player> ALivePs = new ArrayList<>();
    public static ArrayList<Player> Specs = new ArrayList<>();
    
    public static ArrayList<Player> MLG = new ArrayList<>();
    
    public static boolean MLGe = false;
    
    public static HashMap<Player, Integer> Kills = new HashMap<>();
    
    public static ArrayList<Player> Voted = new ArrayList<>();
        
    public static Boolean started = false;
    
    public static ArrayList<Player> Teleported = new ArrayList<>();
    
    public static int need = 6;
    public static int votos = 2;
    public static int nextborder = 60;
    
    
    public static void CheckStart(){
        if (need == 0){
            new Starting(Main.plugin).runTaskTimer(Main.plugin, 02L, 20L);
            Bukkit.broadcastMessage("§6UHCMeetup2.0 Iniciando en §a15s");
            started = true;
        }
    }
    
    public static int GetKills(Player p){
        if (API.Kills.get(p) == null){
            return 0;
        }else{
            return API.Kills.get(p);
        }
    }
    
    public static void CheckWin(Player p){
        if (API.ALivePs.size() == 1){
            Bukkit.broadcastMessage("§a§lGanador del juego §e"+p.getName());
            p.sendMessage("§ePuedes usar §c/mlg§e para probarte en WaterDrops :D");
            States.state = States.FINISH;
        }
    }
    
    public static void Generating(){
        Bukkit.getConsoleSender().sendMessage("§aInjectando biomas...");
        Field biomesField = null;
        try{
          biomesField = BiomeBase.class.getDeclaredField("biomes");
          biomesField.setAccessible(true);
          if ((biomesField.get(null) instanceof BiomeBase[])){
              
            BiomeBase[] biomes = (BiomeBase[])biomesField.get(null);
            biomes[BiomeBase.DEEP_OCEAN.id] = BiomeBase.PLAINS;
            biomes[BiomeBase.OCEAN.id] = BiomeBase.DESERT;
            biomes[BiomeBase.JUNGLE.id] = BiomeBase.PLAINS;
            biomes[BiomeBase.ICE_PLAINS.id] = BiomeBase.PLAINS;
            biomes[BiomeBase.JUNGLE_EDGE.id] = BiomeBase.PLAINS;
            biomes[BiomeBase.JUNGLE_HILLS.id] = BiomeBase.DESERT;
            biomes[BiomeBase.COLD_TAIGA.id] = BiomeBase.PLAINS;
            biomes[BiomeBase.ICE_MOUNTAINS.id] = BiomeBase.DESERT;
            biomes[BiomeBase.FROZEN_RIVER.id] = BiomeBase.PLAINS;
            biomes[BiomeBase.EXTREME_HILLS.id] = BiomeBase.PLAINS;
            biomes[BiomeBase.EXTREME_HILLS_PLUS.id] = BiomeBase.PLAINS;
            biomes[BiomeBase.SWAMPLAND.id] = BiomeBase.DESERT;
            biomes[BiomeBase.TAIGA_HILLS.id] = BiomeBase.PLAINS;
            biomes[BiomeBase.TAIGA.id] = BiomeBase.DESERT;
            biomes[BiomeBase.RIVER.id] = BiomeBase.PLAINS;
            biomes[BiomeBase.SWAMPLAND.id] = BiomeBase.PLAINS;
            biomes[BiomeBase.BEACH.id] = BiomeBase.PLAINS;
            biomes[BiomeBase.ROOFED_FOREST.id] = BiomeBase.DESERT;
            biomes[BiomeBase.COLD_TAIGA_HILLS.id] = BiomeBase.PLAINS;
            biomes[BiomeBase.COLD_TAIGA.id] = BiomeBase.DESERT;
            biomes[BiomeBase.BIRCH_FOREST.id] = BiomeBase.DESERT;
            biomes[BiomeBase.BIRCH_FOREST_HILLS.id] = BiomeBase.PLAINS;
            biomes[BiomeBase.FOREST_HILLS.id] = BiomeBase.DESERT;
            biomes[BiomeBase.SAVANNA.id] = BiomeBase.PLAINS;
            biomes[BiomeBase.SAVANNA_PLATEAU.id] = BiomeBase.PLAINS;
            biomes[BiomeBase.FOREST.id] = BiomeBase.PLAINS;
            biomes[BiomeBase.FOREST_HILLS.id] = BiomeBase.PLAINS;
            biomes[BiomeBase.MEGA_TAIGA.id] = BiomeBase.PLAINS;
            biomes[BiomeBase.MEGA_TAIGA_HILLS.id] = BiomeBase.PLAINS;            
            biomes[BiomeBase.MUSHROOM_ISLAND.id] = BiomeBase.PLAINS;
            biomes[BiomeBase.MUSHROOM_SHORE.id] = BiomeBase.PLAINS;
            
            biomesField.set(null, biomes);
          }
        }
        catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException localException) {
        }
        Bukkit.getConsoleSender().sendMessage("§aBiomas injectados correctamente");
    }
    
    public static void DeleteWorld(String world) {
        File toReset = new File(world);
        World w2 = Bukkit.getWorld(world);
        Bukkit.getServer().unloadWorld(w2, true);
        Bukkit.getConsoleSender().sendMessage("§aReseteando mundo...");
        try {
            FileUtils.deleteDirectory(toReset);
            Bukkit.getConsoleSender().sendMessage("§aMundo borrado");
        } catch (IOException e) {
        }
        Bukkit.createWorld(WorldCreator.name(world));
    }
    
    public static void CheckStartVote(){
        if (votos == 0){
            new Starting(Main.plugin).runTaskTimer(Main.plugin, 02L, 20L);
            Bukkit.broadcastMessage("§6UHCMeetup2.0 Iniciando en §a15s");
            started = true;
        }
    }
    
    static int Online(){
        if (Bukkit.getOnlinePlayers().size() > 9){
            return 9 * 2;
        } else if (Bukkit.getOnlinePlayers().size() > 9 * 2){
            return 9 * 3;
        } else if (Bukkit.getOnlinePlayers().size() > 9 * 3){
            return 9 * 4;
        } else if (Bukkit.getOnlinePlayers().size() > 9 * 4){
            return 9 * 5;
        }else{
            return 9;
        }
    }
    
    public static final Inventory alive = Bukkit.createInventory(null, Online(), "§aJugadores:");
  
    public static Inventory getAlive(){
        API.alive.clear();
        for (Player p : ALivePs){
            Player localPlayer = Bukkit.getServer().getPlayer(p.getName());
            if (localPlayer != null) {
                API.alive.addItem(new ItemStack[] { API.newItem(Material.SKULL_ITEM, p, 3) });
            }
        }
        return API.alive;
    }
    
    
    public static void mlg1(final Player p, int locs) {
        final Random r = new Random();
        final int x = r.nextInt(locs);
        final int z = -r.nextInt(locs);
        final int y = p.getWorld().getHighestBlockYAt(x, z) + 30;
        final World world = Bukkit.getWorld(Main.world);
        final Location randomLoc = new Location(world, (double)x, (double)y, (double)z);
        p.teleport(randomLoc);
    }
    
    public static void mlg2(final Player p, int locs) {
        final Random r = new Random();
        final int x = r.nextInt(locs);
        final int z = -r.nextInt(locs);
        final int y = p.getWorld().getHighestBlockYAt(x, z) + 60;
        final World world = Bukkit.getWorld(Main.world);
        final Location randomLoc = new Location(world, (double)x, (double)y, (double)z);
        p.teleport(randomLoc);
    }
    
    public static void mlg3(final Player p, int locs) {
        final Random r = new Random();
        final int x = r.nextInt(locs);
        final int z = -r.nextInt(locs);
        final int y = p.getWorld().getHighestBlockYAt(x, z) + 90;
        final World world = Bukkit.getWorld(Main.world);
        final Location randomLoc = new Location(world, (double)x, (double)y, (double)z);
        p.teleport(randomLoc);
    }
    
  public static ItemStack newItem(Material paramMaterial, Player paramString, int paramInt){
    ItemStack localItemStack = new ItemStack(paramMaterial, 1, (short)paramInt);
    SkullMeta  meta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.SKULL_ITEM);
    meta.setOwner(paramString.getName());
    meta.setDisplayName("§a"+paramString.getName());
    localItemStack.setItemMeta(meta);

    return localItemStack;
  }
}
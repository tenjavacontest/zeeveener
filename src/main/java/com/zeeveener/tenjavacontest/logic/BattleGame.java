package com.zeeveener.tenjavacontest.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;

import com.zeeveener.tenjavacontest.TenJava;
import com.zeeveener.tenjavacontest.utilities.Chat;

public class BattleGame {

	public static HashMap<Player, BattleGame> games = new HashMap<Player, BattleGame>();
	
	private TenJava plugin;
	private HashMap<EntityType, Integer> kills = new HashMap<EntityType, Integer>();
	private int points = 0;
	private Player p;
	private int threadID;
	private boolean running = false;
	private int deaths = 0;
	private Scoreboard score;
	
	public BattleGame(TenJava instance, Player player){
		plugin = instance;
		p = player;
		
		start();
	}
	
	private void start(){
		int seconds = plugin.config.getConfig().getInt("Game.TimeLimit", 600);
		Chat.message(p, "You have " + (int)seconds/60 + " minutes. Get hunting!");
		threadID = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			@Override
			public void run() {
				p.getInventory().clear();
				for(Entity e : p.getNearbyEntities(50, 50, 50)){
					e.remove();
				}
				List<String> report = new ArrayList<String>();
				report.add("Points Earned: " + points);
				report.add("Deaths: " + deaths);
				report.add("Most Killed Mob: " + Chat.m + getMostKilled().name() + Chat.g + " with " + Chat.m + kills.get(getMostKilled()) + Chat.g + " kills");
			}
		}, seconds*20L);
		String world = plugin.config.getConfig().getString("Game.World");
		p.teleport(plugin.getServer().getWorld(world).getSpawnLocation());
		giveBasicEquipment();
		
		/*
		 * Scoreboard
		 */
		
		running = true;
	}
	
	public static void addPoints(Player p, int toAdd){
		if(!games.containsKey(p)) return;
		games.get(p).addPoints(toAdd);
	}
	public void addPoints(int toAdd){
		points += toAdd;
	}
	
	public static void addDeath(Player p){
		if(!games.containsKey(p)) return;
		games.get(p).addDeath();
	}
	public void addDeath(){
		deaths++;
	}
	
	public static void addKill(Player p, EntityType e){
		if(!games.containsKey(p)) return;
		games.get(p).addKill(e);
	}
	public void addKill(EntityType e){
		int old = 0;
		if(kills.containsKey(e)) old = kills.get(e);
		kills.put(e, ++old);
	}
	
	private void giveBasicEquipment(){
		p.getInventory().clear();
		ItemStack helm = new ItemStack(Material.LEATHER_HELMET, 1);
		ItemStack pant = new ItemStack(Material.LEATHER_LEGGINGS, 1);
		ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
		ItemStack boot = new ItemStack(Material.LEATHER_BOOTS, 1);
		p.getInventory().setArmorContents(new ItemStack[]{boot, pant, chest, helm});
		ItemStack weapon = new ItemStack(Material.STONE_SWORD, 1);
		ItemStack bow = new ItemStack(Material.BOW, 1);
		ItemStack ammo = new ItemStack(Material.ARROW, 24);
		ItemStack food = new ItemStack(Material.COOKED_FISH, 4);
		p.getInventory().addItem(food, weapon, bow, ammo);
	}
	private EntityType getMostKilled(){
		EntityType toReturn = null;
		int lastKillCount = 0;
		for(EntityType et : kills.keySet()){
			if(kills.get(et) > lastKillCount){
				toReturn = et;
				lastKillCount = kills.get(et);
			}
		}
		return toReturn;
	}
}

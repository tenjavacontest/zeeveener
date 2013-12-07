package com.zeeveener.tenjavacontest.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.zeeveener.tenjavacontest.TenJava;
import com.zeeveener.tenjavacontest.utilities.Chat;

public class BattleGame {

	public static HashMap<Player, BattleGame> games = new HashMap<Player, BattleGame>();
	public static World world;
	
	private TenJava plugin;
	private HashMap<EntityType, Integer> kills = new HashMap<EntityType, Integer>();
	private int points = 0;
	private int lastLotteryPoints = 0;
	private int killCount = 0;
	private int lastLotteryKills = 0;
	private Player p;
	private int taskId;
	private int deaths = 0;
	private int lastLotteryDeaths = 0;
	
	private int gameTimeSeconds = 0;
	
	public synchronized static void createGame(Player p, TenJava instance){
		games.put(p, new BattleGame(instance, p));
	}	
	
	public BattleGame(TenJava instance, Player player){
		plugin = instance;
		p = player;
		
		start();
	}
	
	private void start(){
		int seconds = plugin.config.getConfig().getInt("Game.TimeLimit", 600);
		Chat.message(p, "You have " + (int)seconds/60 + " minutes. Get hunting!");
		taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){
			@Override
			public void run() {
				int seconds = plugin.config.getConfig().getInt("Game.TimeLimit", 600);
				gameTimeSeconds++;
				if(gameTimeSeconds >= seconds){
					List<String> report = new ArrayList<String>();
					report.add("Points Earned: " + points);
					report.add("Total Kills: " + killCount);
					if(getMostKilled() != null) report.add("Most Killed Mob: " + getMostKilled().name());
					Chat.message(p, "Game Over!", report.toArray(new String[0]));
					
					stop();
				}else if(seconds - gameTimeSeconds <= 30){
					Chat.message(p, (seconds - gameTimeSeconds) + " seconds left!");
				}
			}
		}, 20l, 20l);
		String w = plugin.config.getConfig().getString("Game.World.world");
		world = plugin.getServer().getWorld(w);
		p.teleport(world.getSpawnLocation());
		giveBasicEquipment();
	}
	public synchronized void stop(){
		if(taskId >= 0) plugin.getServer().getScheduler().cancelTask(taskId);
		plugin.lobby.joinLobby(p);
		games.remove(p);
	}
	
	public synchronized static void setGameWorld(Location loc, TenJava plugin){
		world = loc.getWorld();
		plugin.config.set("Game.World.world", loc.getWorld().getName());
		plugin.config.set("Game.World.x", loc.getX());
		plugin.config.set("Game.World.y", loc.getY());
		plugin.config.set("Game.World.z", loc.getZ());
	}
	public synchronized static World getGameWorld(){
		return world;
	}
	
	public synchronized static void addPoints(Player p, int toAdd){
		if(!games.containsKey(p)) return;
		games.get(p).addPoints(toAdd);
	}
	public void addPoints(int toAdd){
		points += toAdd;
		Chat.message(p, toAdd + " Points!");
		if(points%10 == 0){
			if(points - lastLotteryPoints >= 20){
				if(p.getHealth() < p.getMaxHealth()*0.35) giveFood();
			}
		}
	}
	
	public synchronized static void addDeath(Player p){
		if(!games.containsKey(p)) return;
		games.get(p).addDeath();
	}
	public void addDeath(){
		deaths++;
		if(deaths % 2 == 0){
			if(deaths - lastLotteryDeaths >= 4) giveArmour();
		}
	}
	
	public synchronized static void addKill(Player p, EntityType e){
		if(!games.containsKey(p)) return;
		games.get(p).addKill(e);
	}
	public void addKill(EntityType e){
		int old = 0;
		if(kills.containsKey(e)) old = kills.get(e);
		kills.put(e, ++old);
		killCount++;
		if(killCount % 5 == 0){
			if(killCount - lastLotteryKills >= 10) giveWeapon();
		}
	}
	
	private void giveFood(){
		Material[] lottery = new Material[]{Material.APPLE, Material.BAKED_POTATO, Material.BREAD, Material.CAKE, Material.COOKED_BEEF, Material.COOKED_CHICKEN, Material.COOKED_FISH, Material.COOKIE, Material.MUSHROOM_SOUP};
		Random r = new Random();
		int index = r.nextInt(lottery.length-1);
		int amount = r.nextInt(64);
		p.getInventory().addItem(new ItemStack(lottery[index], amount));
		Chat.message(p, "You have earned " + Chat.m + amount + " " + lottery[index].toString().toLowerCase());
		lastLotteryPoints = points;
	}
	private void giveWeapon(){
		Material[] lottery = new Material[]{Material.STONE_SWORD, Material.IRON_SWORD, Material.DIAMOND_SWORD, Material.GOLD_SWORD, Material.WOOD_SWORD, Material.ARROW};
		Random r = new Random();
		int index = r.nextInt(lottery.length-1);
		int amount = r.nextInt(64);
		p.getInventory().addItem(new ItemStack(lottery[index], amount));
		Chat.message(p, "You have earned " + Chat.m + amount + " " + lottery[index].toString().toLowerCase());
		lastLotteryKills = killCount;
	}
	private void giveArmour(){
		Material[] lottery = new Material[]{Material.IRON_BOOTS, Material.IRON_CHESTPLATE, Material.IRON_HELMET, Material.IRON_LEGGINGS, 
				Material.GOLD_BOOTS, Material.GOLD_CHESTPLATE, Material.GOLD_LEGGINGS, Material.GOLD_HELMET,
				Material.DIAMOND_BOOTS, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_HELMET, Material.DIAMOND_LEGGINGS,
				Material.CHAINMAIL_BOOTS, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_HELMET, Material.CHAINMAIL_LEGGINGS};
		Random r = new Random();
		int index = r.nextInt(lottery.length-1);
		int amount = r.nextInt(64);
		p.getInventory().addItem(new ItemStack(lottery[index], amount));
		Chat.message(p, "You have earned " + Chat.m + amount + " " + lottery[index].toString().toLowerCase());
		lastLotteryDeaths = deaths;
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

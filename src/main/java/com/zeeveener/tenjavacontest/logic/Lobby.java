package com.zeeveener.tenjavacontest.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import com.zeeveener.tenjavacontest.TenJava;

public class Lobby {

	private TenJava plugin;
	public static List<Player> inLobby = new ArrayList<Player>();
	private static HashMap<Player, PlayerInventory> oldInvs = new HashMap<Player, PlayerInventory>();
	private Location spawn;
	
	public Lobby(TenJava instance){
		plugin = instance;
		getDefaultSpawn();
	}
	
	private void getDefaultSpawn(){
		World w = plugin.getServer().getWorld(plugin.config.getConfig().getString("Game.Lobby.world", "world"));
		double x = plugin.config.getConfig().getInt("Game.Lobby.x", 0);
		double y = plugin.config.getConfig().getInt("Game.Lobby.y", 0);
		double z = plugin.config.getConfig().getInt("Game.Lobby.z", 0);
		spawn = new Location(w, x, y, z);
	}
	public Location getSpawn(){
		return spawn;
	}
	public void setSpawn(Location loc){
		spawn = loc;
		plugin.config.getConfig().set("Game.Lobby.world", loc.getWorld().getName());
		plugin.config.getConfig().set("Game.Lobby.x", loc.getX());
		plugin.config.getConfig().set("Game.Lobby.y", loc.getY());
		plugin.config.getConfig().set("Game.Lobby.z", loc.getZ());
	}
	
	public void joinLobby(Player p){
		oldInvs.put(p, p.getInventory());
		p.getInventory().clear();
		p.teleport(spawn);
		addToLobby(p);
	}
	public void leaveLobby(Player p){
		p.getInventory().clear();
		p.getInventory().setContents(oldInvs.get(p).getContents());
		removeFromLobby(p);
	}
	
	public boolean isPlayerInLobby(Player p){
		return inLobby.contains(p);
	}
	public void addToLobby(Player p){
		inLobby.add(p);
	}
	public void removeFromLobby(Player p){
		inLobby.remove(p);
	}
}

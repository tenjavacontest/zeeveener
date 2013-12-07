package com.zeeveener.tenjavacontest.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zeeveener.tenjavacontest.TenJava;
import com.zeeveener.tenjavacontest.logic.BattleGame;
import com.zeeveener.tenjavacontest.utilities.Chat;

public class Setup implements CommandExecutor{

	private TenJava plugin;
	
	public Setup(TenJava instance){
		plugin = instance;
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command command,
			String label, String[] args) {
		
		if(s instanceof Player){
			if(!((Player)s).hasPermission("zeeten.admin.setup")){
				Chat.error(s, "You don't have permission.");
				return false;
			}
		}
		
		if(args.length == 0){
			List<String> help = new ArrayList<String>();
			help.add("The commands are meant to be executed in order from top to bottom.");
			help.add("/setup world (world) (x) (y) (z)");
			help.add("/setup lobby (world) (x) (y) (z)");
			help.add("/setup takeover (t/f)");
			help.add("/setup timelimit (#)");
			help.add("Typing only the first argument will give more info on that arg. (Suggested)");
			Chat.message(s, "Command Info", help.toArray(new String[0]));
		}else if(args.length == 1){
			if(args[0].equalsIgnoreCase("world")){
				List<String> help = new ArrayList<String>();
				help.add("Used to set the world that battles commence within.");
				help.add("/setup world (worldName) (x) (y) (z)");
				help.add("worldName: Name of the world to use");
				help.add("x: X coordinate of the desired spawn point");
				help.add("y: Y coordinate of the desired spawn point");
				help.add("z: Z coordinate of the desired spawn point");
				if(s instanceof Player)help.add("/setup world here - Will set the spawn point at your current location. (Players Only)");
				Chat.message(s, "World Setup Info", help.toArray(new String[0]));
			}else if(args[0].equalsIgnoreCase("lobby")){
				List<String> help = new ArrayList<String>();
				help.add("Used to set the world that the lobby resides within.");
				help.add("/setup lobby (worldName) (x) (y) (z)");
				help.add("worldName: Name of the world to use");
				help.add("x: X coordinate of the desired spawn point");
				help.add("y: Y coordinate of the desired spawn point");
				help.add("z: Z coordinate of the desired spawn point");
				if(s instanceof Player)help.add("/setup lobby here - Will set the spawn point at your current location.");
				Chat.message(s, "Lobby", help.toArray(new String[0]));
			}else if(args[0].equalsIgnoreCase("takeover")){
				List<String> help = new ArrayList<String>();
				help.add("Used to determine if the sole purpose of this server is running this plugin.");
				help.add("Will force players to spawn at the lobby upon login.");
				help.add("/setup takeover (t/f)");
				help.add("t/f: True or False.");
				help.add("By default this is set to false.");
				Chat.message(s, "TakeOver Info", help.toArray(new String[0]));
			}else if(args[0].equalsIgnoreCase("timelimit")){
				List<String> help = new ArrayList<String>();
				help.add("Used to determine how long the game lasts.");
				help.add("/setup timelimit (#)");
				help.add("#: Value in seconds.");
				help.add("By default this is set to 10 minutes (600).");
				Chat.message(s, "Timelimit Info", help.toArray(new String[0]));
			}else{
				s.getServer().dispatchCommand(s, "setup");
				return true;
			}
		}else if(args.length == 2){
			if(args[0].equalsIgnoreCase("world")){
				if(!(s instanceof Player)){
					s.getServer().dispatchCommand(s, "setup");
					return false;
				}
				if(args[1].equalsIgnoreCase("here")){
					Location loc = ((Player)s).getLocation();
					this.world(loc);
					String w = loc.getWorld().getName();
					int x = loc.getBlockX();
					int y = loc.getBlockY();
					int z = loc.getBlockZ();
					Chat.message(s, "World set to: " + w + " at " + x + " " + y + " " + z);
				}else{
					s.getServer().dispatchCommand(s, "setup world");
				}
			}else if(args[0].equalsIgnoreCase("lobby")){
				if(!(s instanceof Player)){
					s.getServer().dispatchCommand(s, "setup");
					return false;
				}
				if(args[1].equalsIgnoreCase("here")){
					Location loc = ((Player)s).getLocation();
					this.lobby(loc);
					String w = loc.getWorld().getName();
					int x = loc.getBlockX();
					int y = loc.getBlockY();
					int z = loc.getBlockZ();
					Chat.message(s, "World set to: " + w + " at " + x + " " + y + " " + z);
				}else{
					s.getServer().dispatchCommand(s, "setup lobby");
				}
			}else if(args[0].equalsIgnoreCase("takeover")){
				if(args[1].equalsIgnoreCase("t") || args[1].equalsIgnoreCase("true")){
					this.server(true);
					Chat.message(s, "TakeOverServer set to TRUE");
				}else if(args[1].equalsIgnoreCase("f") || args[1].equalsIgnoreCase("false")){
					this.server(false);
					Chat.message(s, "TakeOverServer set to FALSE");
				}else{
					s.getServer().dispatchCommand(s, "setup takeover");
				}
			}else if(args[0].equalsIgnoreCase("timelimit")){
				try{
					int x = Integer.parseInt(args[1]);
					plugin.config.set("Game.TimeLimit", x);
					Chat.message(s, "Time limit set to: " + x + " seconds.");
				}catch(NumberFormatException e){
					Chat.error(s, "Expected Integer, got something else.");
					s.getServer().dispatchCommand(s, "setup timelimit");
					return false;
				}
			}else{
				s.getServer().dispatchCommand(s, "setup");
			}
		}else if(args.length == 5){
			if(args[0].equalsIgnoreCase("world")){
				World w = plugin.getServer().getWorld(args[1]);
				int x = 0,y = 0,z = 0;
				try{
					x = Integer.parseInt(args[2]);
					y = Integer.parseInt(args[3]);
					z = Integer.parseInt(args[4]);
				}catch(NumberFormatException e){
					Chat.error(s, "Expected Integer, got something else.");
					s.getServer().dispatchCommand(s, "setup world");
					return false;
				}
				this.world(new Location(w, x, y, z));
				Chat.message(s, "World set to: " + w.getName() + " at " + x + " " + y + " " + z);
			}else if(args[0].equalsIgnoreCase("lobby")){
				World w = plugin.getServer().getWorld(args[1]);
				int x = 0,y = 0,z = 0;
				try{
					x = Integer.parseInt(args[2]);
					y = Integer.parseInt(args[3]);
					z = Integer.parseInt(args[4]);
				}catch(NumberFormatException e){
					Chat.error(s, "Expected Integer, got something else.");
					s.getServer().dispatchCommand(s, "setup lobby");
					return false;
				}
				this.lobby(new Location(w, x, y, z));
				Chat.message(s, "Lobby set to: " + w.getName() + " at " + x + " " + y + " " + z);
			}else{
				s.getServer().dispatchCommand(s, "setup");
			}
		}else{
			s.getServer().dispatchCommand(s, "setup");
		}
		
		return true;
	}
	
	private void world(Location loc){
		loc.getWorld().setSpawnLocation(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		BattleGame.setGameWorld(loc, plugin);
	}
	
	private void server(Boolean b){
		plugin.config.set("Game.TakeOverServer", b);
	}
	
	private void lobby(Location loc){
		loc.getWorld().setSpawnLocation(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		plugin.lobby.setSpawn(loc);
	}
}

package com.zeeveener.tenjavacontest.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zeeveener.tenjavacontest.TenJava;
import com.zeeveener.tenjavacontest.logic.BattleGame;
import com.zeeveener.tenjavacontest.logic.Lobby;
import com.zeeveener.tenjavacontest.utilities.Chat;

public class Game implements CommandExecutor{

	private TenJava plugin;
	public Game(TenJava instance){
		plugin = instance;
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command command,
			String label, String[] args) {
		if(s instanceof Player){
			if(!((Player)s).hasPermission("zeeten.admin.game")){
				Chat.error(s, "You don't have permission");
				return false;
			}
		}
		
		if(args.length == 0){
			List<String> help = new ArrayList<String>();
			help.add("Use these commands to control other players' games.");
			help.add("/game startall");
			help.add("/game start (Player)");
			help.add("/game stopall");
			help.add("/game stop (Player)");
			Chat.message(s, "Game Admin Commands", help.toArray(new String[0]));
		}else if(args.length == 1){
			if(args[0].equalsIgnoreCase("startall")){
				for(Player p : Lobby.inLobby){
					BattleGame.createGame(p, plugin);
				}
			}else if(args[0].equalsIgnoreCase("stopall")){
				for(Player p : BattleGame.games.keySet()){
					BattleGame.games.get(p).stop();
					Chat.message(p, "Your game has been stopped by an Admin.");
				}
			}else if(args[0].equalsIgnoreCase("stop")){
				List<String> help = new ArrayList<String>();
				help.add("Stop games for other players.");
				help.add("/game stopall - Start every currently running game.");
				help.add("/game stop (Player) - Stop a game for (Player)");
				Chat.message(s, "Stop Command Help", help.toArray(new String[0]));
			}else if(args[0].equalsIgnoreCase("start")){
				List<String> help = new ArrayList<String>();
				help.add("Start games for other players.");
				help.add("/game startall - Start a game for everyone in the lobby.");
				help.add("/game start (Player) - Start a game for (Player)");
				Chat.message(s, "Start Command Help", help.toArray(new String[0]));
			}else{
				s.getServer().dispatchCommand(s, "game");
			}
		}else if(args.length == 2){
			Player p;
			if(args[0].equalsIgnoreCase("start")){
				if((p = plugin.getServer().getPlayer(args[1])) != null){
					if(BattleGame.games.containsKey(p)){
						if(Lobby.inLobby.contains(p)){
							BattleGame.createGame(p, plugin);
						}else{
							Chat.error(s, "That player is not in the Lobby.");
						}
					}else{
						Chat.error(s, "That player is already in game.");
					}
				}else{
					Chat.error(s, "Cannot find that player.");
				}
			}else if(args[0].equalsIgnoreCase("stop")){
				if((p = plugin.getServer().getPlayer(args[1])) != null){
					if(BattleGame.games.containsKey(p)){
						if(Lobby.inLobby.contains(p)){
							BattleGame.games.get(p).stop();
							Chat.message(p, "Your game has been ended by an Admin");
						}else{
							Chat.error(s, "That player is not in the Lobby.");
						}
					}else{
						Chat.error(s, "That player is already in game.");
					}
				}else{
					Chat.error(s, "Cannot find that player.");
				}
			}else{
				s.getServer().dispatchCommand(s, "game");
			}
		}else{
			s.getServer().dispatchCommand(s, "game");
		}
		
		return true;
	}
}

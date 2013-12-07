package com.zeeveener.tenjavacontest.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zeeveener.tenjavacontest.TenJava;
import com.zeeveener.tenjavacontest.utilities.Chat;

public class Join implements CommandExecutor{

	private TenJava plugin;
	public Join(TenJava instance){
		plugin = instance;
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command command,
			String label, String[] args) {
		if(!(s instanceof Player)){
			Chat.error(s, "Only players can join the Battle.");
			return true;
		}
		Player p = (Player)s;
		if(!p.hasPermission("zeeten.game.join")){
			Chat.error(s, "You don't have permission to Join the Lobby.");
			return true;
		}
		
		if(!plugin.lobby.isPlayerInLobby(p)){
			plugin.lobby.joinLobby(p);
			Chat.message(p, "Welcome to the Battle Lobby!");
			Chat.message(p, "To start a game, type /start");
		}else{
			Chat.error(p, "You are already in the Lobby.");
		}
		
		return true;
	}
}

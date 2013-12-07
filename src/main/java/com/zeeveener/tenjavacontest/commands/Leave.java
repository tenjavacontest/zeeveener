package com.zeeveener.tenjavacontest.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zeeveener.tenjavacontest.TenJava;
import com.zeeveener.tenjavacontest.utilities.Chat;

public class Leave implements CommandExecutor{

	private TenJava plugin;
	public Leave(TenJava instance){
		plugin = instance;
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command command,
			String label, String[] args) {
		
		if(!(s instanceof Player)){
			Chat.error(s, "Only players can leave the lobby. Use \"/game\" instead.");
			return false;
		}
		
		Player p = (Player)s;
		if(!p.hasPermission("zeeten.game.leave")){
			Chat.error(s, "You don't have permission.");
			return false;
		}
		
		if(!plugin.lobby.isPlayerInLobby(p)){
			Chat.error(s, "You aren't in the Lobby.");
		}else{
			plugin.lobby.leaveLobby(p);
			Chat.message(s, "You have left the Lobby.");
		}
		
		return true;
	}
}

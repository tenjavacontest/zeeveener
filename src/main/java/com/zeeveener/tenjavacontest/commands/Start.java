package com.zeeveener.tenjavacontest.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zeeveener.tenjavacontest.TenJava;
import com.zeeveener.tenjavacontest.logic.BattleGame;
import com.zeeveener.tenjavacontest.utilities.Chat;

public class Start implements CommandExecutor{

	private TenJava plugin;
	public Start(TenJava instance){
		plugin = instance;
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command command,
			String label, String[] args) {
		if(!(s instanceof Player)){
			Chat.error(s, "Only players can start their own game.");
			return true;
		}
		
		Player p = (Player)s;
		if(!p.hasPermission("zeeten.game.start")){
			Chat.error(s, "You don't have permission.");
			return false;
		}
		
		if(plugin.lobby.isPlayerInLobby(p)){
			BattleGame.createGame(p, plugin);
		}else{
			Chat.error(s, "You must be in the lobby to start a game.");
		}
		
		return true;
	}
}

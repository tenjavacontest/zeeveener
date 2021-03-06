package com.zeeveener.tenjavacontest.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.zeeveener.tenjavacontest.TenJava;
import com.zeeveener.tenjavacontest.logic.BattleGame;
import com.zeeveener.tenjavacontest.utilities.Chat;

public class Login implements Listener{

	private TenJava plugin;
	public Login(TenJava instance){
		plugin = instance;
	}
	
	/*
	 * This should be removed in favor of a command to start.
	 */
	@EventHandler
	public void onLogin(PlayerJoinEvent e){
		if(!plugin.config.getConfig().getBoolean("Game.TakeOverServer", false)) return;
		plugin.lobby.joinLobby(e.getPlayer());
	}
	
	@EventHandler
	public void onLogout(PlayerQuitEvent e){
		plugin.lobby.leaveLobby(e.getPlayer());
		BattleGame.games.remove(e.getPlayer());
		Chat.toConsole(e.getPlayer().getName() + " has left their game.");
	}
}

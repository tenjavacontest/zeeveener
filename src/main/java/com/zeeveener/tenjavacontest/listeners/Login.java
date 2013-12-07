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
		BattleGame.games.put(e.getPlayer(), new BattleGame(plugin, e.getPlayer()));
	}
	
	@EventHandler
	public void onLogout(PlayerQuitEvent e){
		BattleGame.games.remove(e.getPlayer());
		Chat.toConsole(e.getPlayer().getName() + " has left their game.");
	}
}

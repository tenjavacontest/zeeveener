package com.zeeveener.tenjavacontest;

import org.bukkit.plugin.java.JavaPlugin;

import com.zeeveener.tenjavacontest.listeners.Battle;
import com.zeeveener.tenjavacontest.utilities.Chat;
import com.zeeveener.tenjavacontest.utilities.Config;

public class TenJava extends JavaPlugin{
	
	public Config config;
	
	public void onEnable(){
		new Chat(this);
		config = new Config(this);
		
		getServer().getPluginManager().registerEvents(new Battle(), this);
		
		Chat.toConsole("You're ready to go!");
	}
	public void onDisable(){
		
	}
}

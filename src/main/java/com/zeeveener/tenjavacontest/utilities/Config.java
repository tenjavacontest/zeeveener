package com.zeeveener.tenjavacontest.utilities;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.zeeveener.tenjavacontest.TenJava;

public class Config {

	private TenJava plugin;
	private File file;
	private FileConfiguration config;
	
	public Config(TenJava instance){
		plugin = instance;
		file = new File(plugin.getDataFolder() + File.separator + "config.yml");
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		config = YamlConfiguration.loadConfiguration(file);
		
		init();
	}
	
	private void init(){
		if(!config.contains("Game.World")){
			set("Game.World.world", "world");
			set("Game.World.x", 0);
			set("Game.World.y", 0);
			set("Game.World.z",	0);
		}
		if(!config.contains("Game.TimeLimit")) set("Game.TimeLimit", 600);
		if(!config.contains("Game.Lobby")){
			set("Game.Lobby.world", "world");
			set("Game.Lobby.x", 0);
			set("Game.Lobby.y", 0);
			set("Game.Lobby.z", 0);
		}
		if(!config.contains("Game.TakeOverServer")) set("Game.TakeOverServer", false);
	}
	
	public File getFile(){
		return file;
	}
	public FileConfiguration getConfig(){
		return config;
	}
	private void save(){
		config.options().header("Game:"
				+ "\n\tWorld: Which world will the game take place in?"
				+ "\n\tTimeLimit: Time in seconds that the game will last for."
				+ "\n\tLobby: The location at which people will go when not in battle. Use \"/set lobby\" in game to set this easily"
				+ "\n\tTakeOverServer: Do you want to force players to spawn in the lobby upon login? This assumes you only want to run the server as this plugins minigame.");
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void set(String path, Object value){
		config.set(path, value);
		save();
	}
}

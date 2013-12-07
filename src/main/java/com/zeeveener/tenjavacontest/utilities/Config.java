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
		file = new File(plugin.getDataFolder() + "config.yml");
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
		if(!config.contains("Game.World")) set("Game.World", "world");
		if(!config.contains("Game.TimeLimit")) set("Game.TimeLimit", 600);
	}
	
	public File getFile(){
		return file;
	}
	public FileConfiguration config(){
		return config;
	}
	private void save(){
		config.options().header("Game:"
				+ "\n\tWorld: Which world will the game take place in?"
				+ "\n\tTimeLimit: Time in seconds that the game will last for.");
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

package com.zeeveener.tenjavacontest.utilities;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import com.zeeveener.tenjavacontest.TenJava;

public class Chat {

	//private static TenJava plugin;
	private static Logger log;
	public Chat(TenJava instance){
		//plugin = instance;
		log = Logger.getLogger("Minecraft");
	}
	
	public static ChatColor m = ChatColor.GOLD;
	public static ChatColor g = ChatColor.GRAY;
	public static ChatColor e = ChatColor.RED;
	
	public static void toConsole(String message){
		log.info("[ZeeTenJava] " + ChatColor.stripColor(message));
	}
	public static void message(CommandSender s, String message){
		if(s instanceof ConsoleCommandSender){
			toConsole(message);
		}else{
			s.sendMessage(Chat.m + "[ZTJ] " + Chat.g + message);
		}
	}
	
	public static void message(CommandSender s, String top, String[] message){
		message(s, Chat.m + "<<< " + Chat.g + top + Chat.m + " >>>");
		for(String str : message){
			message(s, str);
		}
	}
	
	public static void error(CommandSender s, String error){
		if(s instanceof ConsoleCommandSender){
			toConsole(error);
		}else{
			s.sendMessage(Chat.e + "[ZTJError] " + error);
		}
	}
}

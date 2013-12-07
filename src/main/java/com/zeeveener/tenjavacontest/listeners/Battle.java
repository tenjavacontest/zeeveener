package com.zeeveener.tenjavacontest.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.zeeveener.tenjavacontest.logic.BattleGame;

public class Battle implements Listener{

	
	@EventHandler
	public void onMobDeath(EntityDeathEvent e){
		Entity dead = e.getEntity();
		Player killer = e.getEntity().getKiller();
		if(killer == null) return;
		int pointsAwarded = points(dead);
		if(pointsAwarded <= 0) return;
		
		BattleGame.addKill(killer, dead.getType());
		BattleGame.addPoints(killer, pointsAwarded);
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e){
		BattleGame.addDeath(e.getEntity());
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e){
		if(!BattleGame.games.containsKey(e.getPlayer())) return;
		e.setRespawnLocation(BattleGame.getGameWorld().getSpawnLocation());
	}
	
	private int points(Entity e){
		if(!isMob(e)) return 0;
		
		switch(e.getType()){
		case SKELETON:
			return 5;
		case SPIDER:
			return 5;
		case ZOMBIE:
			return 5;
		case SLIME:
			return 5;
		case GIANT:
			return 50;
		case CREEPER:
			return 10;
		case ENDERMAN:
			return 15;
		case GHAST:
			return 20;
		case MAGMA_CUBE:
			return 5;
		case PIG_ZOMBIE:
			return 5;
		case WITHER:
			return 25;
		case BLAZE:
			return 25;
		case WITHER_SKULL:
			return 100;
		case IRON_GOLEM:
			return 15;
		case SNOWMAN:
			return 10;
		default:
			break;
		}
		
		return 0;
	}
	
	private boolean isMob(Entity e){
		switch(e.getType()){
		case SKELETON:
			break;
		case SPIDER:
			break;
		case ZOMBIE:
			break;
		case SLIME:
			break;
		case GIANT:
			break;
		case CREEPER:
			break;
		case ENDERMAN:
			break;
		case GHAST:
			break;
		case MAGMA_CUBE:
			break;
		case PIG_ZOMBIE:
			break;
		case WITHER:
			break;
		case BLAZE:
			break;
		case WITHER_SKULL:
			break;
		case IRON_GOLEM:
			break;
		case SNOWMAN:
			break;
		default:
			return false;
		}
		return true;
	}
}
package com.aetheriumwars.stickytracker.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.aetheriumwars.stickytracker.StickyTracker;

public class OnPlayerQuit implements Listener{
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Bukkit.getLogger().info(event.getPlayer().getDisplayName()+" quit");
		StickyTracker.removeTracker(event.getPlayer());
	}

}

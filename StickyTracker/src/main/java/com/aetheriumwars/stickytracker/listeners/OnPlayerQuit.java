package com.aetheriumwars.stickytracker.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.aetheriumwars.stickytracker.StickyTracker;

public class OnPlayerQuit implements Listener{
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		StickyTracker.removeTrackerOwnedBy(event.getPlayer());
		StickyTracker.removeTrackerAttachedTo(event.getPlayer());
	}

}

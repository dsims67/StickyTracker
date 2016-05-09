package com.aetheriumwars.stickytracker.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.aetheriumwars.stickytracker.tracker.Tracker;

public class OnPlayerQuit implements Listener{
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		//remove the trail if a player logs off
		if(Tracker.hasTracker(event.getPlayer())) {
			Tracker.getTracker(event.getPlayer()).hide();
		}
		
		Tracker t = Tracker.isBeingTracked(event.getPlayer());
		if(t != null) {
			t.hide();
		}

	}

}

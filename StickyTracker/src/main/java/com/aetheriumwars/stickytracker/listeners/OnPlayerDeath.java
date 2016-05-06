package com.aetheriumwars.stickytracker.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.aetheriumwars.stickytracker.tracker.Tracker;

public class OnPlayerDeath implements Listener {
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		Player p = event.getEntity();
		if(Tracker.isBeingTracked(p) != null) {
			Tracker.removeTrackerAttachedTo(p);
		}
	}

}

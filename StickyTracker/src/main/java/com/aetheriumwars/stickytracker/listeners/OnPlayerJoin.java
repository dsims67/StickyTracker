package com.aetheriumwars.stickytracker.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.aetheriumwars.stickytracker.StickyTracker;
import com.aetheriumwars.stickytracker.tracker.Tracker;

public class OnPlayerJoin implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		//if the player has a tracker or is being tracked and the owner is online, show the trail
		Player p = event.getPlayer();
		if(Tracker.hasTracker(p)) {
			Tracker t = StickyTracker.getTrackers().get(p);
			
			if(t.getTarget() != null)
				t.show();
		}
		
		Tracker t = Tracker.isBeingTracked(event.getPlayer());
		boolean ownerOnline = Bukkit.getServer().getOfflinePlayer(t.getOwnerID()).isOnline();
		if(t != null && ownerOnline) {
			t.show();
		}
	}

}

package com.aetheriumwars.stickytracker.listeners;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.aetheriumwars.stickytracker.tracker.Tracker;

public class OnPlayerJoin implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		//if the player has a tracker or is being tracked and the owner is online, show the trail
		Player p = event.getPlayer();
		//if the player owns a tracker and the target is online
		if(Tracker.hasTracker(p)) {
			Tracker t = Tracker.getTracker(p);
			
			if(t != null && t.getTarget() != null)
				t.show();
		}
		
		Tracker t = Tracker.isBeingTracked(event.getPlayer());
		
		if(t != null) {
			OfflinePlayer owner = Bukkit.getServer().getOfflinePlayer(t.getOwnerID());
			if(owner != null && owner.isOnline())
				t.show();
		}
	}

}

package com.aetheriumwars.stickytracker.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.aetheriumwars.stickytracker.StickyTracker;
import com.aetheriumwars.stickytracker.enums.EnumPermission;
import com.aetheriumwars.stickytracker.tracker.Tracker;

public class OnTrackerInteract implements Listener {
	
	@EventHandler
	public void onTrackerRightClick(PlayerInteractEvent ev) {
		
		Player p = ev.getPlayer();
		//if player right clicks with the tracker in their hand, toggle the visibility of the trail
		if(ev.getAction().equals(Action.RIGHT_CLICK_AIR) || ev.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if(EnumPermission.ST_USE.hasPermission(p) 
					&& p.getItemInHand().getTypeId() == StickyTracker.getTrackerItemId()
					&& Tracker.hasTracker(p)) {
				
				Tracker t = Tracker.getTracker(p);
				if(t.getTarget() == null || !t.getTarget().isOnline())
					return;
				
				if(t.hiddenByPlayer() && t.isHidden()) {
					t.show();
					t.setHiddenByPlayer(false);
					p.sendMessage("§bTracker Trail: §aVisible");
				}
				else {
					t.hide();
					t.setHiddenByPlayer(true);
					p.sendMessage("§bTracker Trail: §cHidden");
				}
			}
		}

	}

}

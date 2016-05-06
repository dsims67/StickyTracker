package com.aetheriumwars.stickytracker.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.scheduler.BukkitRunnable;

import com.aetheriumwars.stickytracker.StickyTracker;
import com.aetheriumwars.stickytracker.tracker.Tracker;

public class OnFireDamage implements Listener{

	@EventHandler
	public void onPlayerDamage(EntityDamageEvent ev) {
		if(ev.getCause().equals(DamageCause.FIRE_TICK) && ev.getEntity() instanceof Player) {
			final Player p = (Player) ev.getEntity();
			if(Tracker.isBeingTracked(p) != null) {
				//remove the tracker after 1 second
				new BukkitRunnable() {
					@Override
					public void run() {
						Tracker.removeTrackerAttachedTo(p);
					}
				}.runTaskLater(StickyTracker.getPlugin(), 20);
			}
		}
	}
}

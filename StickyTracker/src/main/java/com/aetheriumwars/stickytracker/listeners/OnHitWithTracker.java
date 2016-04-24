package com.aetheriumwars.stickytracker.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.aetheriumwars.stickytracker.StickyTracker;
import com.aetheriumwars.stickytracker.tracker.Tracker;

import net.md_5.bungee.api.ChatColor;

public class OnHitWithTracker implements Listener{
	
	@EventHandler
	public void onPlayerDamage(EntityDamageByEntityEvent ev) {
		if(ev.getEntity() instanceof Player && ev.getDamager() instanceof Player) {
			Player tracker = (Player) ev.getDamager();
			Player trackee = (Player) ev.getEntity();
			Bukkit.getLogger().info("Material: "+tracker.getItemInHand().getType().toString()+" id: "+tracker.getItemInHand().getTypeId());
			//if(tracker.getItemInHand().getType().equals(StickyTracker.trackerItem)) {
			if(tracker.getItemInHand().getTypeId() == 151) {
				if(StickyTracker.hasTracker(tracker)) {
					StickyTracker.removeTrackerOwnedBy(tracker);
				}
				
				StickyTracker.addTracker(new Tracker(tracker, trackee));
				if(tracker.getItemInHand().getAmount() > 1)
					tracker.getItemInHand().setAmount(tracker.getItemInHand().getAmount()-1);
				else {
					tracker.setItemInHand(new ItemStack(Material.AIR));
					tracker.updateInventory();
				}
				tracker.sendMessage(ChatColor.GREEN+"Tracker successfully attached to "+trackee.getDisplayName());
			}
		}
	}

}

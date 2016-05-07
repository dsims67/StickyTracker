package com.aetheriumwars.stickytracker.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.aetheriumwars.stickytracker.StickyTracker;
import com.aetheriumwars.stickytracker.enums.EnumPermission;
import com.aetheriumwars.stickytracker.tracker.Tracker;

import net.md_5.bungee.api.ChatColor;

public class OnHitWithTracker implements Listener{
	
	@EventHandler
	public void onPlayerDamage(EntityDamageByEntityEvent ev) {
		if(ev.getEntity() instanceof Player && ev.getDamager() instanceof Player) {
			Player tracker = (Player) ev.getDamager();
			Player trackee = (Player) ev.getEntity();
			int trackerId = StickyTracker.getTrackerItemId();
			
			//if the player does not have permissions to use the trackers, don't allow them to attach it
			if(EnumPermission.ST_USE.hasPermission(tracker) == false) {
				return;
			}
			else if(EnumPermission.ST_UNTRACKABLE.hasPermission(trackee)) {
				tracker.sendMessage(ChatColor.RED+"Trackers cannot be attached to this player");
				return;
			}
			
			//Bukkit.getLogger().info("Material: "+tracker.getItemInHand().getType().toString()+" id: "+tracker.getItemInHand().getTypeId());
			if(tracker.getItemInHand().getTypeId() == trackerId) {
				if(Tracker.hasTracker(tracker)) {
					Tracker.removeTrackerOwnedBy(tracker);
				}
				
				Tracker.addTracker(new Tracker(tracker, trackee));
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

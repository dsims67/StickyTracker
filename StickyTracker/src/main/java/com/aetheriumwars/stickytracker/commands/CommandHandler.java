package com.aetheriumwars.stickytracker.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.aetheriumwars.stickytracker.StickyTracker;
import com.aetheriumwars.stickytracker.tracker.Tracker;

import net.md_5.bungee.api.ChatColor;

public class CommandHandler implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) { 
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.isOp() && command.getName().equalsIgnoreCase("stickytracker") && args != null && args.length >= 1) {
					if (args[0].equalsIgnoreCase("test") && args.length >= 2) {
						Player target = Bukkit.getPlayer(args[1]);
						if(target == null || player == null) {
							sender.sendMessage("§a§lStickyTracker: &cPlayer not found");
							return false;
						}
						new Tracker(player, target).generateTrail();
						sender.sendMessage("§a§lStickyTracker: "+ChatColor.AQUA+"testing the tracking effect with "+target.getDisplayName());
						return true;
					}
					else if(args[0].equalsIgnoreCase("clear")) {
						StickyTracker.removeTrackerOwnedBy(player);
						sender.sendMessage("§a§lStickyTracker: "+ChatColor.AQUA+"removing your trackers.");
						return true;
					}
					else if(args[0].equalsIgnoreCase("list")) { //list the trackers
						for(Tracker t : StickyTracker.getTrackers().values()) {
							sender.sendMessage("Owner: "+t.getOwner().getDisplayName()+" Target: "+t.getTarget().getDisplayName());
						}
						return true;
					}
			}
			
			player.sendMessage("§a§lStickyTracker: §cCommand not found.");
			printHelp(player);
		}
		
		return false;
	}
	
    public void printHelp(Player p) {
    	p.sendMessage("§l§oST: /test <player>  -> test the particle trail");
    	p.sendMessage("§l§oST: /list -> list all of the trackers active");
    	p.sendMessage("§l§oST: /clear -> removes trackers you own");
    }
		
}

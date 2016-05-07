package com.aetheriumwars.stickytracker.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.aetheriumwars.stickytracker.StickyTracker;
import com.aetheriumwars.stickytracker.enums.EnumPermission;
import com.aetheriumwars.stickytracker.tracker.Tracker;

import net.md_5.bungee.api.ChatColor;

public class CommandHandler implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) { 
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (EnumPermission.ST_ADMIN.hasPermission(player) && command.getName().equalsIgnoreCase("stickytracker") && args != null && args.length >= 1) {
					if (args[0].equalsIgnoreCase("track") && args.length >= 2) {
						Player origin;
						Player target;
						
						if(args.length == 3) {
							origin = Bukkit.getServer().getPlayer(args[1]);
							target = Bukkit.getPlayer(args[2]);
						}
						else {
							origin = player;
							target = Bukkit.getPlayer(args[1]);
						}				
						
						if(target == null || origin == null) {
							sender.sendMessage("§a§lStickyTracker: §cPlayer not found");
							return false;
						}
						new Tracker(player, target).generateTrail();
						sender.sendMessage("§a§lStickyTracker: "+ChatColor.AQUA+"testing the tracking effect with "+target.getDisplayName());
						return true;
					}
					else if(args[0].equalsIgnoreCase("clear")) {
						Tracker.removeTrackerOwnedBy(player);
						sender.sendMessage("§a§lStickyTracker: "+ChatColor.AQUA+"removing your trackers.");
						return true;
					}
					else if(args[0].equalsIgnoreCase("list")) { //list the trackers
						sender.sendMessage("§a§lStickyTracker: §bActive Trackers");
						for(Tracker t : StickyTracker.getTrackers().values()) {
							sender.sendMessage("§aOwner: §2"+t.getOwner().getName()+" §cTarget: §4"+t.getTarget().getName());
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
    	p.sendMessage("§lST: §o/track (player) <target>  -> place a tracker on the target");
    	p.sendMessage("§lST: §o/list -> list all of the trackers active");
    	p.sendMessage("§lST: §o/clear -> removes trackers you own");
    }
		
}

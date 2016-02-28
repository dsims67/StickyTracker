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

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.isOp() && command.getName().equalsIgnoreCase("stickytracker") && args != null && args.length >= 1) {
                if (args[0].equalsIgnoreCase("test") && args.length >= 2) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if(target == null) {
                        sender.sendMessage("§a§lStickyTracker: §cPlayer not found");
                        return false;
                    }
                    if(target == player) {
                        sender.sendMessage("§a§lStickyTracker: §cYou cant track yourself");
                        return false;
                    }
                    new Tracker(player, target).generateTrail();
                    sender.sendMessage("§a§lStickyTracker: "+ChatColor.AQUA+"testing the tracking effect with "+target.getDisplayName());
                    return true;
                }
                else if(args[0].equalsIgnoreCase("clear")) {
                    StickyTracker.removeTracker(player);
                    sender.sendMessage("§a§lStickyTracker: "+ChatColor.AQUA+"removing your trackers.");
                    return true;
                }
                else if(args[0].equalsIgnoreCase("list")) { //list the trackers
                    if(StickyTracker.getTrackers().isEmpty()) return true;

                    sender.sendMessage("§a§m=======================================");
                    for(Tracker t : StickyTracker.getTrackers().values()) {
                        sender.sendMessage("§eOwner§7: §c"+t.getOwner().getDisplayName()+"§7- §eTarget§7: §c"+t.getTarget().getDisplayName());
                    }
                    sender.sendMessage("§a§m=======================================");
                    return true;
                }
            }

            player.sendMessage("§a§lStickyTracker: &cCommand not found.");
            printHelp(player);
        }

        return false;
    }

    public void printHelp(Player p) {
        p.sendMessage("§l§oST: /test -> test the particle trail");
    }

}

package com.aetheriumwars.stickytracker;

import java.io.File;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import com.aetheriumwars.stickytracker.commands.CommandHandler;
import com.aetheriumwars.stickytracker.tracker.Tracker;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.util.DynamicLocation;

public class StickyTracker extends JavaPlugin{
	
	private static StickyTracker st;
	private static FileConfiguration config;
	private static Plugin plugin;
	private static EffectManager effectManager;
	
	private static HashMap<UUID, Tracker> trackers = new HashMap<UUID, Tracker>();
	
	private static int closestProximity = 10; //when the tracker gets within this distance of the trackee, the particles disappear
	
	@Override
	public void onEnable() {
		st = this;	
		effectManager = new EffectManager(this);
		
		//init config
		this.saveDefaultConfig();
		config = this.getConfig();
		//this.getConfig().options().copyDefaults(true);
		
		File configFile = new File(getPlugin().getDataFolder()+File.separator+"config.yml");
		/*if(!configFile.exists())
			initConfig();*/
		
		//make trackerdata folder
		boolean successful = new File(getPlugin().getDataFolder()+File.separator+"TrackerData").mkdir();
		if(successful)
			getLogger().info("Created TrackerData Directory");
		
		//register listeners
		//registerListener(new BindAbilityListener());

		//register commands
        getCommand("stickytracker").setExecutor(new CommandHandler());
		
        //for aliases
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("strack");
        getCommand("stickytracker").setAliases(arrayList);
        
		getLogger().info("StickyTracker has been enabled");
		
		/*tracker scheduler*/
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
            	//save every 5 mins
            	if(trackers.size() > 0) {
	            	for(Tracker t: trackers.values()) {
	            		if(!t.isHidden()) {
	            			//if the distance is less than the closest proximity, hide the particles
	            			if(t.getOwner().getLocation().distance(t.getTarget().getLocation()) <= closestProximity) {
	            				t.removeTrail();
	            			}
	            			else {
		            			t.getEffect().setDynamicOrigin(new DynamicLocation(t.getOwner().getLocation()));
		            			t.getEffect().setDynamicTarget(new DynamicLocation(t.getTarget().getLocation().add(0,11,0)));
	            			}
	            		}
	            		else {
	            			if(t.getOwner().getLocation().distance(t.getTarget().getLocation()) > closestProximity && t.isHidden()) {
	            				t.generateTrail();
	            			}
	            		}
	            		
	            	}
	            	
            	}
            }
        }, 1L, 1L);
	}
	
	@Override
	public void onDisable() {
		effectManager.dispose();
		HandlerList.unregisterAll();
		this.saveConfig();
		getLogger().info("StickyTracker has been disabled!");
	}
	
	public static Plugin getPlugin() {
		return st;
	}
	
	public static EffectManager getEffectManager() {
		return effectManager;
	}
	
	public static void addTracker(Tracker t) {
		//a player can only have 1 tracker active at a time
		if(trackers.containsKey(t.getOwnerID()))
			removeTracker(t.getOwner());
		
		trackers.put(t.getOwnerID(), t);
	}
	
	public static void removeTracker(Player p) {
		if(trackers.containsKey(p.getUniqueId())) {
			trackers.get(p.getUniqueId()).removeTrail();
			trackers.remove(p.getUniqueId());
		}

	}
	
	public static HashMap<UUID, Tracker> getTrackers() {
		return trackers;
	}
	
}

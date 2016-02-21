package com.aetheriumwars.stickytracker;

import java.io.File;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class StickyTracker extends JavaPlugin{
	
	private static StickyTracker st;
	private static FileConfiguration config;
	private static Plugin plugin;
	
	@Override
	public void onEnable() {
		st = this;	
		
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
        //getCommand("bendexp").setExecutor(new CommandHandler());
		
        //for aliases
        //ArrayList<String> arrayList = new ArrayList<>();
        //arrayList.add("bxp");
        //getCommand("bendexp").setAliases(arrayList);
        
		getLogger().info("StickyTracker has been enabled");
		
		/*auto-save scheduler*/
        /*BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
            	//save every 5 mins
            	if(playerData.size() > 0) {
	            	getLogger().info("Saving player data..");
	            	for(BenderData b: playerData.values()) {
	            		b.saveBenderJSON();
	            	}
            	}
            }
        }, 0L, 6000L);*/
	}
	
	@Override
	public void onDisable() {
		this.saveConfig();
		getLogger().info("StickyTracker has been disabled!");
	}
	
	public static Plugin getPlugin() {
		return st;
	}
	
}

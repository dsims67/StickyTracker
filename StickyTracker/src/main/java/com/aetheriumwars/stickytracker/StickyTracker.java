package com.aetheriumwars.stickytracker;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.aetheriumwars.stickytracker.commands.CommandHandler;
import com.aetheriumwars.stickytracker.listeners.OnFireDamage;
import com.aetheriumwars.stickytracker.listeners.OnHitWithTracker;
import com.aetheriumwars.stickytracker.listeners.OnPlayerDeath;
import com.aetheriumwars.stickytracker.listeners.OnPlayerJoin;
import com.aetheriumwars.stickytracker.listeners.OnPlayerQuit;
import com.aetheriumwars.stickytracker.tracker.Tracker;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.util.DynamicLocation;

public class StickyTracker extends JavaPlugin{
	
	private static StickyTracker st;
	private static FileConfiguration config;
	private static Plugin plugin;
	private static EffectManager effectManager;
	
	private static HashMap<UUID, Tracker> trackers = new HashMap<UUID, Tracker>();
	
	public static Material trackerItem = Material.DAYLIGHT_DETECTOR;
	private static int closestProximity = 10; //when the tracker gets within this distance of the trackee, the particles disappear
	private static String trackerFilename = "trackers.json";
	
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
		File trackerFile = new File(getPlugin().getDataFolder()+File.separator+trackerFilename);
		if(!trackerFile.exists()) {
			try {
				trackerFile.createNewFile();
				getLogger().info("Created "+trackerFilename+" file.");
			} catch (IOException e) {
				getLogger().log(Level.SEVERE, e.getMessage(), e);
			}
			
		}
		else {
			trackers = loadTrackers();
		}
		
		//register listeners
		registerListener(new OnPlayerJoin());
		registerListener(new OnPlayerQuit());
		registerListener(new OnHitWithTracker());
		registerListener(new OnFireDamage());
		registerListener(new OnPlayerDeath());

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
	            		if(t == null || t.getOwner() == null || t.getTarget() == null)
	            			continue;
	            		
            			Player owner = (Player) t.getOwner();
            			Player target = (Player) t.getTarget();
            			
	            		if(!t.isHidden()) {
	            			
	            			//if the distance is less than the closest proximity, hide the particles
	            			if(owner.getLocation().distance(target.getLocation()) <= closestProximity) {
	            				t.hide();
	            			}
	            			else {
	            				
	            				Location startLoc = owner.getLocation();
	            				Location endLoc = target.getLocation();
	            				
	            				Location midpoint = new Location(owner.getWorld(), (startLoc.getX()+endLoc.getX())/2, 
	            						(startLoc.getY()+endLoc.getY())/2, (startLoc.getZ()+endLoc.getZ())/2);
	            				
	            				//System.out.println("Starting at: "+startLoc.toString()+" Ending at: "+midpoint.toString());
	            				t.getEffect().setDynamicOrigin(new DynamicLocation(owner.getLocation()));
	            				t.getEffect().setDynamicTarget(new DynamicLocation(midpoint));
	            				
	            			}
	            		}
	            		else {
	            			if(owner.getLocation().distance(target.getLocation()) > closestProximity && t.isHidden()) {
	            				t.generateTrail();
	            			}
	            		}
	            		
	            	}
	            	
            	}
            }
        }, 10L, 2L);
        
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
	
	public static HashMap<UUID, Tracker> getTrackers() {
		return trackers;
	}
	
	public static void saveTrackers() {
		JSONObject json = new JSONObject();
		
		for(Tracker t: trackers.values()) {
			json.put(t.getOwnerID().toString(), t.getTargetID().toString());
		}
		
		try {
			FileWriter file = new FileWriter(getPlugin().getDataFolder()+File.separator+trackerFilename);
			file.write(json.toJSONString());
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	private static HashMap<UUID, Tracker> loadTrackers() {
		HashMap<UUID, Tracker> map = new HashMap<UUID, Tracker>();
		String fileLoc = getPlugin().getDataFolder()+File.separator+trackerFilename;
		JSONParser parser = new JSONParser();
		try {
			Object obj;
			obj = parser.parse(new FileReader(fileLoc));
			JSONObject json = (JSONObject) obj;
			
			for(Iterator iterator = json.keySet().iterator(); iterator.hasNext();) {
			    String ownerId = (String) iterator.next();
			    String targetId = (String) json.get(ownerId);
			    map.put(UUID.fromString(ownerId), new Tracker(ownerId, targetId));
			}
			
			return map;
			
		} catch (IOException ex) {
			ex.printStackTrace();
			return map;
		} catch(ParseException ex) {
			return map;
		}
	}
	
	private void registerListener(Listener l) {
		this.getServer().getPluginManager().registerEvents(l, this);
	}
	
}

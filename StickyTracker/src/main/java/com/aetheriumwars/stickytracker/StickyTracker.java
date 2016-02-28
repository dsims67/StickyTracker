package com.aetheriumwars.stickytracker;

import java.io.File;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitScheduler;

import com.aetheriumwars.stickytracker.commands.CommandHandler;
import com.aetheriumwars.stickytracker.tracker.Tracker;
import com.aetheriumwars.stickytracker.listeners.OnHitWithTracker;
import com.aetheriumwars.stickytracker.listeners.OnPlayerQuit;

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
        registerListener(new OnPlayerQuit());
        registerListener(new OnHitWithTracker());

        //register commands
        getCommand("stickytracker").setExecutor(new CommandHandler());

        //for aliases
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("strack");
        getCommand("stickytracker").setAliases(arrayList);

        getLogger().info("StickyTracker has been enabled");
		
		/*tracker scheduler*/
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {

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

                                Location startLoc = t.getOwner().getLocation();
                                Location endLoc = t.getTarget().getLocation();

                                Location midpoint = new Location(t.getOwner().getWorld(), (startLoc.getX()+endLoc.getX())/2,
                                        (startLoc.getY()+endLoc.getY())/2, (startLoc.getZ()+endLoc.getZ())/2);

                                //System.out.println("Starting at: "+startLoc.toString()+" Ending at: "+midpoint.toString());
                                t.getEffect().setDynamicOrigin(new DynamicLocation(t.getOwner().getLocation()));
                                t.getEffect().setDynamicTarget(new DynamicLocation(midpoint));

                                if(t.isDebugState()){
                                    t.getOwner().sendMessage("§aYour position§7:§e " + t.getOwner().getLocation().getBlockX() + "§7,§e " + t.getOwner().getLocation().getBlockY() + "§7,§e " + t.getOwner().getLocation().getBlockZ());
                                    t.getOwner().sendMessage("§aTarget position§7:§e " + t.getTarget().getLocation().getBlockX() + "§7,§e " + t.getTarget().getLocation().getBlockY() + "§7,§e " + t.getTarget().getLocation().getBlockZ());
                                    t.getOwner().sendMessage("");
                                    t.getOwner().sendMessage("§aLine Effect Owner location§7:§e " + startLoc.getBlockX() + "§7,§e " + startLoc.getBlockY() + "§7,§e " + startLoc.getBlockZ());
                                    t.getOwner().sendMessage("§aLine Effect Target location§7:§e " + endLoc.getBlockX() + "§7,§e " + endLoc.getBlockY() + "§7,§e " + endLoc.getBlockZ());
                                    t.getOwner().sendMessage("§aLine Effect Midpoint location§7:§e " + midpoint.getBlockX() + "§7,§e " + midpoint.getBlockY() + "§7,§e " + midpoint.getBlockZ());
                                }

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

    public static boolean hasTracker(Player p) {
        return(trackers.containsKey(p.getUniqueId()));
    }

    public static HashMap<UUID, Tracker> getTrackers() {
        return trackers;
    }

    private void registerListener(Listener l) {
        this.getServer().getPluginManager().registerEvents(l, this);
    }

}

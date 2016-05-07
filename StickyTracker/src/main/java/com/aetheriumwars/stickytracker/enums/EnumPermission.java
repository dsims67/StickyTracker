package com.aetheriumwars.stickytracker.enums;

import org.bukkit.entity.Player;

public enum EnumPermission {

	 ST_ADMIN("stickytracker.admin"), //gives user permission to use commands
	 ST_USE("stickytracker.use"), //allows user to place trackers
	 ST_UNTRACKABLE("stickytracker.untrackable"); //prevents user from being tracked (bypassed by command)
	 
	 private final String permission;
	 
	 private EnumPermission(String permission) {
		 this.permission = permission;
	 }
	 
	 public String getPermission() {
		 return permission;
	 }
	 
	 public boolean hasPermission(Player p) {
		 return p.hasPermission(getPermission());
	 }

}

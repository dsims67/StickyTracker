package com.aetheriumwars.stickytracker.enums;

public enum EnumPermission {

	 ST_ADMIN("stickytracker.admin");
	 
	 private final String permission;
	 
	 private EnumPermission(String permission) {
		 this.permission = permission;
	 }
	 
	 public String getPermission() {
		 return permission;
	 }

}

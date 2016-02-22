package com.aetheriumwars.stickytracker.tracker;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.aetheriumwars.stickytracker.StickyTracker;

import de.slikey.effectlib.effect.LineEffect;
import de.slikey.effectlib.util.DynamicLocation;
import de.slikey.effectlib.util.ParticleEffect;

public class Tracker {
	
	private static int visibility = 7; //how far can you see the path
	
	private UUID ownerId;
	private UUID targetId;
	private LineEffect lineEffect;
	
	public Tracker(Player owner, Player target) {
		this.ownerId = owner.getUniqueId();
		this.targetId = target.getUniqueId();
		this.generateTrail();
		StickyTracker.addTracker(this);
	}
	
	public UUID getOwnerID() {
		return ownerId;
	}
	//the player who placed the tracker
	public Player getOwner() {
		return Bukkit.getPlayer(ownerId);
	}
	
	//the player being tracked
	public Player getTarget() {
		return Bukkit.getPlayer(targetId);
	}
	
	public LineEffect getEffect() {
		return lineEffect;
	}
	
	public boolean isHidden() {
		if(lineEffect == null)
			return true;
		else
			return false;
	}
	
	public void removeTrail() {
		if(lineEffect != null) {
			lineEffect.cancel();
			this.lineEffect = null;
		}
	}
	
	//generates and updates the trail of particles leading to the player
	public void generateTrail() {
		this.lineEffect = new LineEffect(StickyTracker.getEffectManager());
		//lineEffect.particle = ParticleEffect.REDSTONE;
		lineEffect.period = 1;
		lineEffect.visibleRange = visibility;
		lineEffect.isZigZag = false;
		lineEffect.updateLocations = true;
		lineEffect.updateDirections = true;
		//loc.addOffset(getOwner().getVelocity().setY(getOwner().getLocation().getY() - getOwner().getEyeHeight()) );
		lineEffect.iterations = -1;
		lineEffect.particle = ParticleEffect.PORTAL;
		lineEffect.setDynamicOrigin(new DynamicLocation(this.getOwner().getLocation()));
		lineEffect.setDynamicTarget(new DynamicLocation(this.getTarget().getLocation().add(0, 11, 0)));
		//lineEffect.particleOffsetX = 1;
		//lineEffect.particleOffsetY = 1;
		lineEffect.start();
	}

}

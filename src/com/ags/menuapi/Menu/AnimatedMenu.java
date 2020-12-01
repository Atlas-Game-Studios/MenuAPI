package com.ags.menuapi.Menu;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.ags.menuapi.callbacks.AnimationCallback;
import com.ags.menuapi.callbacks.ClickCallback;
import com.ags.menuapi.decoration.Decoration;

public class AnimatedMenu extends CallbackMenu implements Runnable {
	
	AnimationCallback animation;
	int animationSpeed = 100;
	
	Player viewer;
	
	int taskNumber;
	
	public AnimatedMenu(JavaPlugin plugin, String name, MenuSize menusize, Decoration decoration, int animationSpeed) {
		super(plugin, name, menusize, 1, decoration);
		this.animationSpeed = animationSpeed;
		taskNumber = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, 60, animationSpeed);
	}
	
	public AnimationCallback getAnimation() {
		return animation;
	}

	public void setAnimation(AnimationCallback animation) {
		this.animation = animation;
	}

	public int getTickSpeed() {
		return animationSpeed;
	}

	public Player getViewer() {
		return viewer;
	}

	public void setViewer(Player viewer) {
		this.viewer = viewer;
	}
	
	@Override
	public void close(Player player) {
		plugin.getServer().getScheduler().cancelTask(taskNumber);
		super.close(player);
	}
	
	@Override
	public void open(Player player) {
		if(viewer != null) close(viewer);
		setViewer(player);
		super.open(player);
	}

	@Override
	public void run() {
		if(animation != null && viewer != null) {
			animation.callback(this);
		}	
	}

}

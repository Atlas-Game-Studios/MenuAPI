package com.ags.menuapi.Menu;

import com.ags.menuapi.callbacks.AnimationCallback;
import com.ags.menuapi.decoration.Decoration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class AnimatedListMenu extends ListMenu implements Runnable {

    AnimationCallback animation;

    int animationSpeed;

    Player viewer;

    int taskNumber;

    public AnimatedListMenu(JavaPlugin plugin, String name, MenuSize menusize, int pages, Decoration decoration, int min, int max) {
        this(plugin, name, menusize, pages, decoration, min, max, 60);
    }

    public AnimatedListMenu(JavaPlugin plugin, String name, MenuSize menuSize, int pages, Decoration decoration, int min, int max, int animationSpeed) {
        super(plugin, name, menuSize, pages, decoration, min, max);
        this.animationSpeed = animationSpeed;
        taskNumber = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, 20, animationSpeed);
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
        if (viewer != null) close(viewer);
        setViewer(player);
        super.open(player);
    }

    @Override
    public void run() {
        if (animation != null && viewer != null) {
            animation.callback(this);
        }
    }
}

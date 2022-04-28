package com.ags.menuapi.Menu;

import com.ags.menuapi.MenuItem.MenuItem;
import com.ags.menuapi.decoration.Decoration;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unchecked")
public class GameMenu extends AnimatedMenu implements Runnable {

    HashMap<String, Object> gameVariables;

    public GameMenu(JavaPlugin plugin, String name, MenuSize menusize, Decoration decoration, int animationSpeed) {
        super(plugin, name, menusize, decoration, animationSpeed);
        gameVariables = new HashMap<String, Object>();
    }

    public void setVar(String name, Object variable) {
        gameVariables.put(name, variable);
    }

    public Object getVar(String name) {
        return gameVariables.get(name);
    }

    public String getVarString(String name) {
        return (String) gameVariables.get(name);
    }

    public Integer getVarInt(String name) {
        return (Integer) gameVariables.get(name);
    }

    public Boolean getVarBool(String name) {
        return (Boolean) gameVariables.get(name);
    }

    public Double getVarDoub(String name) {
        return (Double) gameVariables.get(name);
    }

    public ItemStack getVarItem(String name) {
        return (ItemStack) gameVariables.get(name);
    }

    public MenuItem getVarMItem(String name) {
        return (MenuItem) gameVariables.get(name);
    }

    public Location getVarLoc(String name) {
        return (Location) gameVariables.get(name);
    }

    public Player getVarPlayer(String name) {
        return (Player) gameVariables.get(name);
    }

    public List<String> getVarStringList(String name) {
        try {
            return (List<String>) gameVariables.get(name);
        } catch (ClassCastException e) {
            plugin.getLogger().info(ChatColor.RED + "You are trying to retrieve a game variable of a different type than the method used.");
            e.printStackTrace();
            return null;
        }
    }

    public List<Integer> getVarIntList(String name) {
        try {
            return (List<Integer>) gameVariables.get(name);
        } catch (ClassCastException e) {
            plugin.getLogger().info(ChatColor.RED + "You are trying to retrieve a game variable of a different type than the method used.");
            e.printStackTrace();
            return null;
        }
    }

    public List<Boolean> getVarBoolList(String name) {
        try {
            return (List<Boolean>) gameVariables.get(name);
        } catch (ClassCastException e) {
            plugin.getLogger().info(ChatColor.RED + "You are trying to retrieve a game variable of a different type than the method used.");
            e.printStackTrace();
            return null;
        }
    }

    public List<Double> getVarDoubList(String name) {
        try {
            return (List<Double>) gameVariables.get(name);
        } catch (ClassCastException e) {
            plugin.getLogger().info(ChatColor.RED + "You are trying to retrieve a game variable of a different type than the method used.");
            e.printStackTrace();
            return null;
        }
    }

    public List<ItemStack> getVarItemList(String name) {
        try {
            return (List<ItemStack>) gameVariables.get(name);
        } catch (ClassCastException e) {
            plugin.getLogger().info(ChatColor.RED + "You are trying to retrieve a game variable of a different type than the method used.");
            e.printStackTrace();
            return null;
        }
    }

    public List<MenuItem> getVarMItemList(String name) {
        try {
            return (List<MenuItem>) gameVariables.get(name);
        } catch (ClassCastException e) {
            plugin.getLogger().info(ChatColor.RED + "You are trying to retrieve a game variable of a different type than the method used.");
            e.printStackTrace();
            return null;
        }
    }

    public List<Location> getVarLocList(String name) {
        try {
            return (List<Location>) gameVariables.get(name);
        } catch (ClassCastException e) {
            plugin.getLogger().info(ChatColor.RED + "You are trying to retrieve a game variable of a different type than the method used.");
            e.printStackTrace();
            return null;
        }
    }

    public List<Player> getVarPlayerList(String name) {
        try {
            return (List<Player>) gameVariables.get(name);
        } catch (ClassCastException e) {
            plugin.getLogger().info(ChatColor.RED + "You are trying to retrieve a game variable of a different type than the method used.");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void run() {
        if (animation != null && viewer != null) {
            animation.callback(this);
        }
    }

}

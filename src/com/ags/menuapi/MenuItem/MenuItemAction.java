package com.ags.menuapi.MenuItem;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.ags.menuapi.Menu.Menu;
import com.ags.menuapi.Menu.MenuPage;

public interface MenuItemAction {
    public void action(MenuPage page, Player clicker, InventoryClickEvent event);
}

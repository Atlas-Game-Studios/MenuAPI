package com.ags.menuapi.callbacks;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.ags.menuapi.Menu.Menu;
import com.ags.menuapi.Menu.MenuPage;

public interface ClickCallback {

	void callback(Menu menu, MenuPage page, ItemStack item, int slot, Player clicker, InventoryClickEvent event);
	
}

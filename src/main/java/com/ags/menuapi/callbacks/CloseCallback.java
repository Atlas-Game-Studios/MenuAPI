package com.ags.menuapi.callbacks;

import com.ags.menuapi.Menu.Menu;
import com.ags.menuapi.Menu.MenuPage;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public interface CloseCallback {

    void callback(Menu menu, MenuPage page, ItemStack item, int slot, Player clicker, InventoryCloseEvent event);

}

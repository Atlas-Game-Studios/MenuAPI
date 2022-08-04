package com.ags.menuapi.MenuItem;

import com.ags.menuapi.Menu.MenuPage;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public interface MenuItemAction {
    void action(MenuPage page, Player clicker, InventoryClickEvent event);
}

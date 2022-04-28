package com.ags.menuapi.Menu;

import com.ags.menuapi.MenuItem.MenuItem;
import com.ags.menuapi.MenuItem.MenuItemAction;
import com.ags.menuapi.decoration.Decoration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

public class ActionMenu extends Menu {

    HashMap<ItemStack, MenuItemAction> actions;

    public ActionMenu(JavaPlugin plugin, String name, MenuSize menusize, int pages, Decoration decoration) {
        this.plugin = plugin;
        this.name = name;
        this.menusize = menusize;
        this.size = menusize.toNumber();
        this.pages = new ArrayList<MenuPage>();
        this.decoration = decoration;
        actions = new HashMap<ItemStack, MenuItemAction>();

        for (int i = 0; i < pages; i++) {
            addPage();
        }

    }

    @Override
    public void addItemToPage(int page, int slot, MenuItem mItem) {
        super.addItemToPage(page, slot, mItem);
        actions.put(mItem.getItem(), mItem.getAction());
    }

    @Override
    public void addItemsToPage(int page, HashMap<Integer, MenuItem> mItems) {
        super.addItemsToPage(page, mItems);
        for (MenuItem mItem : mItems.values()) {
            actions.put(mItem.getItem(), mItem.getAction());
        }
    }

    public void handleItemClick(MenuPage page, ItemStack item, int slot, Player clicker, InventoryClickEvent event) {
        super.handleItemClick(page, item, slot, clicker, event);
        if (actions.containsKey(item) && actions.get(item) != null) {
            actions.get(item).action(page, clicker, event);
        }
    }

}

package com.ags.menuapi.Menu;

import com.ags.menuapi.MenuItem.MenuItem;
import com.ags.menuapi.callbacks.ClickCallback;
import com.ags.menuapi.decoration.Decoration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Map;

public class CallbackMenu extends Menu {

    ClickCallback callback;

    public CallbackMenu(JavaPlugin plugin, String name, MenuSize menusize, int pages, Decoration decoration) {
        this.plugin = plugin;
        this.name = name;
        this.menusize = menusize;
        this.size = menusize.toNumber();
        this.pages = new ArrayList<MenuPage>();
        this.decoration = decoration;
        for (int i = 0; i < pages; i++) {
            addPage();
        }
    }

    public void setCallbackHandler(ClickCallback callback) {
        this.callback = callback;
    }

    /**
     * addInteractToPage() - This method adds a MenuItem (often empty) in a specific slot on a page in a menu.
     * This item is an interact slot. This allows the MenuItem to be moved or have another item added to the slot.
     * Remember to use the finish() function to finish a menu after adding all MenuItems to it.
     * <br>NOTE: Items put in slots that go beyond the size of this menu will default to the last slot.
     *
     * @param page  - The page to add this Interact to.
     * @param slot  - The slot on the page to add the Interact to.
     * @param interact - The Interact to add.
     */
    public void addInteractToPage(int page, int slot, MenuItem interact) {
        pages.get(page).addInteract(slot, interact);
    }

    /**
     * addInteractsToPage() This method adds a MenuItem (often empty) in a specific slot on a page in a menu.
     * This item is an interact slot. This allows the MenuItem to be moved or have another item added to the slot.
     * Remember to use the finish() function to finish a menu after adding all MenuItems to it.
     * <br>NOTE: Items put in slots that go beyond the size of this menu will default to the last slot.
     *
     * @param page      - The page to add this Interact to.
     * @param interacts - A HashMap<Integer,MenuItem> corresponding to multiple (slot,MenuItem) interact pairs to add to the menu.
     */
    public void addInteractsToPage(int page, Map<Integer, MenuItem> interacts) {
        MenuPage menupage = pages.get(page);
        menupage.addInteracts(interacts);
    }

    /**
     * getInteractOnPage() - This method returns the Interact on a slot on a specific page.
     *
     * @param page - The page on which to look at the slot for a MenuItem
     * @param slot - The slot to look for an item in.
     * @return The Interact in the slot given. Can return null.
     */
    public MenuItem getInteractOnPage(int page, int slot) {
        if (pages.get(page).getInteracts().containsKey(slot)) {
            return pages.get(page).getItem(slot);
        }
        return null;

    }


    public void handleItemClick(MenuPage page, ItemStack item, int slot, Player clicker, InventoryClickEvent event) {
        super.handleItemClick(page, item, slot, clicker, event);
        callback.callback(this, page, item, slot, clicker, event);
    }


}

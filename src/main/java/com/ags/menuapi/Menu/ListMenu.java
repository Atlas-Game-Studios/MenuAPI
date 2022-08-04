package com.ags.menuapi.Menu;

import com.ags.menuapi.MenuItem.MenuItem;
import com.ags.menuapi.decoration.Decoration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;


public class ListMenu extends ActionMenu {


    List<MenuItem> listItems;

    int min;
    int max;


    public ListMenu(JavaPlugin plugin, String name, MenuSize menusize, int pages, Decoration decoration) {
        super(plugin, name, menusize, pages, decoration);
        listItems = new ArrayList<>();
        min = 0;
        max = menusize.toNumber() - 1;
        overrideDecoration = false;
    }

    public ListMenu(JavaPlugin plugin, String name, MenuSize menusize, int pages, Decoration decoration, int min, int max) {
        this(plugin, name, menusize, pages, decoration);
        this.min = min;
        this.max = max;
        overrideDecoration = false;
    }

    public int setItemOnList(MenuItem mItem, int startSlot) {

        int minSlot = min;
        int maxSlot = max;

        if (startSlot > min) minSlot = startSlot;
        if (startSlot > max) return -1;

        // This effectively limits the size of a listMenu to 100 pages.
        while (pages.size() < 100) {
            MenuPage mPage = getLastPage();
            for (int slot = minSlot; slot <= maxSlot; slot++) {
                // check if there are any menu items in this slot

                if (!mPage.getItems().containsKey(slot)) {

                    // Check if either override decorations is true (so decorations don't matter)
                    // or if there are no decorations in this slot.
                    if (overrideDecoration || !mPage.getDecorationSlots().contains(slot)) {
                        setListItem(mItem, mPage, slot);
                        return slot;
                    }
                }
            }
            minSlot = min;
            addPage();
        }
        return -1;
    }


    private void setListItem(MenuItem mItem, MenuPage mPage, int slot) {
        super.addItemToPage(mPage.getPagenumber(), slot, mItem);
        listItems.add(mItem);
        //mPage.setItem(slot,mItem);
    }

    public int setItemToList(MenuItem mItem) {
        int slot = setItemOnList(mItem, 0);
        updateItem(mItem);
        return slot;
    }

    public int setItemsToList(List<MenuItem> mItems) {
        if (mItems.size() == 0) return 0;
        int currentslot = 0;
        for (MenuItem mItem : mItems) {
            currentslot = setItemOnList(mItem, currentslot);
        }
        return currentslot;
    }

    public int updateItemOnList(MenuItem mItem, int startSlot) {

        int minSlot = min;
        int maxSlot = max;

        if (startSlot > min) minSlot = startSlot;
        if (startSlot > max) return -1;

        // This effectively limits the size of a listMenu to 100 pages.
        while (pages.size() < 100) {
            MenuPage mPage = getLastPage();
            for (int slot = minSlot; slot <= maxSlot; slot++) {
                // check if there are any menu items in this slot

                if (!mPage.getItems().containsKey(slot)) {

                    // Check if either override decorations is true (so decorations don't matter)
                    // or if there are no decorations in this slot.
                    if (overrideDecoration || !mPage.getDecorationSlots().contains(slot)) {
                        updateListItem(mItem, mPage, slot);
                        return slot;
                    }
                }
            }
            minSlot = min;
            addPage();
        }
        return -1;
    }

    public int updateItemToList(MenuItem mItem) {
        int slot = updateItemOnList(mItem, 0);
        updateItem(mItem);
        return slot;
    }

    private void updateListItem(MenuItem mItem, MenuPage mPage, int slot) {
        super.addItemToPage(mPage.getPagenumber(), slot, mItem);
    }


    public int updateItemsToList(List<MenuItem> mItems) {
        if (mItems.size() == 0) return 0;
        int currentslot = 0;
        for (MenuItem mItem : mItems) {
            currentslot = updateItemOnList(mItem, currentslot);
        }
        return currentslot;
    }

//	public int updateItemsToList(CopyOnWriteArrayList<MenuItem> mItems) {
//		if(mItems.size() == 0) return 0;
//		int currentslot = 0;
//		for(MenuItem mItem: mItems) {
//			currentslot = updateItemOnList(mItem,currentslot);
//		}
//		return currentslot;
//	}

    public void removeItemFromList(MenuItem mItem) {

        // First physically remove all List items from this menu
        for (MenuItem listItem : listItems) {
            removeItem(listItem);
        }

        // Next remove the specific mItem from the listItems
        listItems.remove(mItem);

        // Re-add all remaining List Items to the menu
        updateItemsToList(listItems);

    }

    public void removeItemsFromList(List<MenuItem> mItems) {
        // First physically remove all List items from this menu
        for (MenuItem listItem : listItems) {
            removeItem(listItem);
        }

        // Next remove the specific mItems from the listItems
        listItems.removeAll(mItems);

        // Re-add all remaining List Items to the menu

        updateItemsToList(listItems);
    }

    public void handleItemClick(MenuPage page, ItemStack item, int slot, Player clicker, InventoryClickEvent event) {
        super.handleItemClick(page, item, slot, clicker, event);
    }

}

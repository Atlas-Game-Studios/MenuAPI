package com.ags.menuapi.addons;

import com.ags.menuapi.Menu.Menu;
import com.ags.menuapi.Menu.MenuSize;
import com.ags.menuapi.MenuItem.MenuItem;

import java.util.HashMap;

public class BackButton {

    // This is the static information retrieved from the config file.
    // These are defaults that can be changed for any individual menu.
    private static HashMap<MenuSize, Integer> backButtonSlots = new HashMap<MenuSize, Integer>();
    private static MenuItem backButtonItem;

    public static void load(HashMap<MenuSize, Integer> slots, MenuItem item) {
        backButtonSlots = slots;
        backButtonItem = item;
    }

    private boolean isActive;
    private MenuItem buttonItem;
    private Integer buttonSlot;
    private Menu backMenu;

    public BackButton(Menu holdingMenu) {
        isActive = false;
        buttonItem = backButtonItem;
        buttonSlot = backButtonSlots.get(holdingMenu.getMenuSize());
    }

    public BackButton(Menu holdingMenu, Menu backMenu) {
        isActive = true;
        buttonItem = backButtonItem;
        buttonSlot = backButtonSlots.get(holdingMenu.getMenuSize());
        this.backMenu = backMenu;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean set) {
        isActive = set;
    }

    public MenuItem getItem() {
        return buttonItem;
    }

    public void setItem(MenuItem buttonItem) {
        this.buttonItem = buttonItem;
    }

    public Integer getSlot() {
        return buttonSlot;
    }

    public void setSlot(Integer buttonSlot) {
        this.buttonSlot = buttonSlot;
    }

    public Menu getBackMenu() {
        return backMenu;
    }

    public void setBackMenu(Menu backMenu) {
        this.backMenu = backMenu;
    }


}

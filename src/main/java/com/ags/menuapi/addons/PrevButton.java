package com.ags.menuapi.addons;

import com.ags.menuapi.Menu.Menu;
import com.ags.menuapi.Menu.MenuSize;
import com.ags.menuapi.MenuItem.MenuItem;

import java.util.HashMap;

public class PrevButton {

    // This is the static information retrieved from the config file.
    // These are defaults that can be changed for any individual menu.
    private static HashMap<MenuSize, Integer> prevButtonSlots = new HashMap<MenuSize, Integer>();
    private static MenuItem prevButtonItem;

    public static void load(HashMap<MenuSize, Integer> slots, MenuItem item) {
        prevButtonSlots = slots;
        prevButtonItem = item;
    }

    private MenuItem buttonItem;
    private Integer buttonSlot;

    public PrevButton(Menu holdingMenu) {
        buttonItem = prevButtonItem;
        buttonSlot = prevButtonSlots.get(holdingMenu.getMenuSize());
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


}

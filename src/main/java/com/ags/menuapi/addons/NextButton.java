package com.ags.menuapi.addons;

import com.ags.menuapi.Menu.Menu;
import com.ags.menuapi.Menu.MenuSize;
import com.ags.menuapi.MenuItem.MenuItem;

import java.util.HashMap;
import java.util.Map;

public class NextButton {

    // This is the static information retrieved from the config file.
    // These are defaults that can be changed for any individual menu.
    private static Map<MenuSize, Integer> nextButtonSlots = new HashMap<>();
    private static MenuItem nextButtonItem;

    public static void load(Map<MenuSize, Integer> slots, MenuItem item) {
        nextButtonSlots = slots;
        nextButtonItem = item;
    }


    private MenuItem buttonItem;
    private Integer buttonSlot;

    public NextButton(Menu holdingMenu) {
        buttonItem = nextButtonItem;
        buttonSlot = nextButtonSlots.get(holdingMenu.getMenuSize());
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

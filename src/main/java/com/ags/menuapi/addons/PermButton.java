package com.ags.menuapi.addons;

import com.ags.menuapi.MenuItem.MenuItem;

public class PermButton {


    private MenuItem buttonItem;
    private Integer buttonSlot;

    public PermButton(int slot, MenuItem mItem) {
        buttonItem = mItem;
        buttonSlot = slot;
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

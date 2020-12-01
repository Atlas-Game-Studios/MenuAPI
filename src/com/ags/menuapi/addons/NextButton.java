package com.ags.menuapi.addons;

import java.util.HashMap;

import com.ags.menuapi.Menu.Menu;
import com.ags.menuapi.Menu.MenuSize;
import com.ags.menuapi.MenuItem.MenuItem;

public class NextButton {

	// This is the static information retrieved from the config file.
	// These are defaults that can be changed for any individual menu.
	private static HashMap<MenuSize,Integer> nextButtonSlots = new HashMap<MenuSize,Integer>();
	private static MenuItem nextButtonItem;
	public static void load(HashMap<MenuSize,Integer> slots, MenuItem item) {
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

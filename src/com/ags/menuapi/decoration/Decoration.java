package com.ags.menuapi.decoration;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.ags.menuapi.Menu.MenuSize;

public class Decoration {

	ItemStack[] itemMap;
	MenuSize size;
	
	private Decoration(MenuSize size) {
		this.size = size;
		this.itemMap = new ItemStack[size.toNumber()];
	}
	
	/**
	 * Decoration - Create a new Decoration with a pre-configured scheme.
	 * @param size - The size of the menu. This must match the size of menu you created
	 * @param mat - the Material to use as the decoration item
	 * @param scheme - the Scheme for the decorations.
	 */
	public Decoration(MenuSize size, Material mat, Scheme scheme) {
		this(size);
		setScheme(mat,scheme);
	}
	
	/**
	 * Decoration - Create a new Decoration with a custome scheme.
	 * @param size - The size of the menu. This must match the size of menu you created
	 * @param mat - the Material to use as the decoration item
	 * @param slots - The custom List of slots to put the decoration Material in.
	 */
	public Decoration(MenuSize size, Material mat, ArrayList<Integer> slots) {
		this(size);
		setCustomScheme(mat,slots);
	}
	
	public ItemStack[] getItemMap() {
		return itemMap;
	}


	private void setScheme(Material mat, Scheme scheme) {
		for(int i = 0; i < itemMap.length; i++) {
			itemMap[i] = new ItemStack(Material.AIR);
		}
		for(int slot: scheme.getSlotsArray(size)) {
			ItemStack item = new ItemStack(mat);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(" ");
			item.setItemMeta(meta);
			itemMap[slot] = item;
		}
	}
	
	private void setCustomScheme(Material mat, ArrayList<Integer> slots) {
		for(int i = 0; i < itemMap.length; i++) {
			itemMap[i] = new ItemStack(Material.AIR);
		}
		for(int slot: slots) {
			ItemStack item = new ItemStack(mat);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(" ");
			item.setItemMeta(meta);
			itemMap[slot] = item;
		}
	}
}

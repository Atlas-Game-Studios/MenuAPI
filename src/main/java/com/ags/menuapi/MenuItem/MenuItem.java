package com.ags.menuapi.MenuItem;

import com.ags.menuapi.Menu.MenuPage;
import com.ags.menuapi.util.NBTUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MenuItem {

    private MenuItemAction action;
    private ItemStack item;

    private MenuPage page;
    private int slot;

    public MenuItem(ItemStack item) {
        this.item = item;
        setMeta(null, null);
    }

    public MenuItem(ItemStack item, String name) {
        this(item);
        setMeta(name, null);
    }

    public MenuItem(ItemStack item, String name, String... lore) {
        this(item);
        List<String> loreList = Arrays.asList(lore);
        setMeta(name, loreList);
    }

    public MenuItem(ItemStack item, String name, List<String> lore) {
        this(item);
        setMeta(name, lore);
    }

    public MenuItem(Material mat) {
        this.item = new ItemStack(mat);
    }

    public MenuItem(Material mat, String name) {
        this(mat);
        setMeta(name, null);
    }

    public MenuItem(Material mat, String name, String... lore) {
        this(mat);
        List<String> loreList = new ArrayList<>(Arrays.asList(lore));
        setMeta(name, loreList);
    }

    public MenuItem(Material mat, String name, List<String> lore) {
        this(mat);
        setMeta(name, lore);
    }

    public MenuItem(MenuItemAction action, ItemStack item) {
        this(item);
        this.action = action;
    }

    public MenuItem(MenuItemAction action, ItemStack item, String name) {
        this(action, item);
        setMeta(name, null);
    }

    public MenuItem(MenuItemAction action, ItemStack item, String name, String... lore) {
        this(action, item);
        List<String> loreList = Arrays.asList(lore);
        setMeta(name, loreList);
    }

    public MenuItem(MenuItemAction action, ItemStack item, String name, List<String> lore) {
        this(action, item);
        setMeta(name, lore);
    }

    public MenuItem(MenuItemAction action, Material mat) {
        this(mat);
        this.action = action;
    }

    public MenuItem(MenuItemAction action, Material mat, String name) {
        this(action, mat);
        setMeta(name, null);
    }

    public MenuItem(MenuItemAction action, Material mat, String name, String... lore) {
        this(action, mat);
        List<String> loreList = Arrays.asList(lore);
        setMeta(name, loreList);
    }

    public MenuItem(MenuItemAction action, Material mat, String name, List<String> lore) {
        this(action, mat);
        setMeta(name, lore);
    }

    private void setMeta(String name, List<String> lore) {
        ItemMeta meta = this.item.getItemMeta();
        if (name != null) meta.setDisplayName(name);
        if (lore != null) meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        this.item.setItemMeta(meta);
    }


    public MenuPage getPage() {
        return page;
    }

    public void setPage(MenuPage page) {
        this.page = page;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public MenuItemAction getAction() {
        return action;
    }

    public void setAction(MenuItemAction action) {
        this.action = action;
    }

    public MenuItem highlight() {
        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        return this;
    }

    public MenuItem unhighlight() {
        ItemMeta meta = item.getItemMeta();
        meta.removeEnchant(Enchantment.DURABILITY);
        item.setItemMeta(meta);
        return this;
    }

    public boolean isHighlighted() {
        ItemMeta meta = item.getItemMeta();
        return meta.hasEnchants();
    }

    public MenuItem setNBTTag(String tag, Object value) {
        item = NBTUtil.addTag(item, tag, value);
        return this;
    }

    public Integer getNBTInt(String tag) {
        return NBTUtil.getInteger(item, tag);
    }

    public Boolean getNBTBool(String tag) {
        return NBTUtil.getBoolean(item, tag);
    }

    public String getNBTString(String tag) {
        return NBTUtil.getString(item, tag);
    }

    public boolean hasNBTTag(String tag) {
        return NBTUtil.hasKey(item, tag);
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public String getName() {
        return item.getItemMeta().getDisplayName();
    }

    public void setName(String name) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
    }

    public int getAmount() {
        return item.getAmount();
    }

    public void setAmount(int amount) {
        item.setAmount(amount);
    }

    public List<String> getDesc() {
        return item.getItemMeta().getLore();
    }

    public void setDesc(List<String> lines) {
        ItemMeta meta = item.getItemMeta();
        meta.setLore(lines);
        item.setItemMeta(meta);
    }

}

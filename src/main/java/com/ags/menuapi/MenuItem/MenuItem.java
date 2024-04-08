package com.ags.menuapi.MenuItem;

import com.ags.atlaslib.util.NBTUtil;
import com.ags.menuapi.Menu.MenuPage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class MenuItem {

    private MenuItemAction action;
    private ItemStack item;

    private MenuPage page;
    private int slot;

    public MenuItem(ItemStack item) {
        this.item = item;
        setMeta(null, null, null);
    }

    public MenuItem(ItemStack item, String name) {
        this(item);
        setMeta(name, null, null);
    }

    public MenuItem(ItemStack item, String name, String... lore) {
        this(item);
        List<String> loreList = Arrays.asList(lore);
        setMeta(name, loreList, null);
    }

    public MenuItem(ItemStack item, String name, List<String> lore) {
        this(item);
        setMeta(name, lore, null);
    }

    public MenuItem(ItemStack item, String name, List<String> lore, Integer model) {
        this(item);
        setMeta(name, lore, model);
    }

    public MenuItem(Material mat) {
        this.item = new ItemStack(mat);
    }

    public MenuItem(Material mat, String name) {
        this(mat);
        setMeta(name, null, null);
    }

    public MenuItem(Material mat, String name, String... lore) {
        this(mat);
        List<String> loreList = Arrays.asList(lore);
        setMeta(name, loreList, null);
    }

    public MenuItem(Material mat, String name, List<String> lore) {
        this(mat);
        setMeta(name, lore, null);
    }

    public MenuItem(MenuItemAction action, ItemStack item) {
        this(item);
        this.action = action;
    }

    public MenuItem(MenuItemAction action, ItemStack item, String name) {
        this(action, item);
        setMeta(name, null, null);
    }

    public MenuItem(MenuItemAction action, ItemStack item, String name, String... lore) {
        this(action, item);
        List<String> loreList = Arrays.asList(lore);
        setMeta(name, loreList, null);
    }

    public MenuItem(MenuItemAction action, ItemStack item, String name, List<String> lore) {
        this(action, item);
        setMeta(name, lore, null);
    }

    public MenuItem(MenuItemAction action, Material mat) {
        this(mat);
        this.action = action;
    }

    public MenuItem(MenuItemAction action, Material mat, String name) {
        this(action, mat);
        setMeta(name, null, null);
    }

    public MenuItem(MenuItemAction action, Material mat, String name, String... lore) {
        this(action, mat);
        List<String> loreList = Arrays.asList(lore);
        setMeta(name, loreList, null);
    }

    public MenuItem(MenuItemAction action, Material mat, String name, List<String> lore) {
        this(action, mat);
        setMeta(name, lore, null);
    }

    private void setMeta(String name, List<String> lore, Integer model) {
        var mm = MiniMessage.miniMessage();
        ItemMeta meta = this.item.getItemMeta();
        if (meta == null) return;
        if (name != null) meta.displayName(mm.deserialize(name));
        if (lore != null) meta.lore(lore.stream().map(mm::deserialize).toList());
        if (model != null) meta.setCustomModelData(model);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
        this.item.setItemMeta(meta);
        NBTUtil.setTag(item, "menu", true);
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
        NBTUtil.setTag(item, tag, value);
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
        Component display = item.getItemMeta().displayName();
        return PlainTextComponentSerializer.plainText().serialize(display);
    }

    public void setName(String name) {
        var mm = MiniMessage.miniMessage();
        ItemMeta meta = item.getItemMeta();
        meta.displayName(mm.deserialize(name));
        item.setItemMeta(meta);
    }

    public int getAmount() {
        return item.getAmount();
    }

    public void setAmount(int amount) {
        item.setAmount(amount);
    }

    public List<String> getDesc() {
        List<Component> lore = item.getItemMeta().lore();
        if (lore == null) return new ArrayList<>();
        var mm = MiniMessage.miniMessage();
        return lore.stream().map(mm::serialize).collect(Collectors.toList());
    }

    public void setDesc(List<String> lines) {
        var mm = MiniMessage.miniMessage();
        ItemMeta meta = item.getItemMeta();
        meta.lore(lines.stream().map(mm::deserialize).toList());
        item.setItemMeta(meta);
    }

}

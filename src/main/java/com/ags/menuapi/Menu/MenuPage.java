package com.ags.menuapi.Menu;

import com.ags.atlaslib.util.MessageUtil;
import com.ags.atlaslib.util.PlayerUtil;
import com.ags.menuapi.MenuItem.MenuItem;
import com.ags.menuapi.addons.ClickSound;
import com.ags.menuapi.decoration.Decoration;
import com.ags.menuapi.events.MenuCloseEvent;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MenuPage implements Listener {

    private final Inventory inv;
    private final int pagenumber;
    private final Set<Integer> decorationSlots;
    private final HashBiMap<Integer, MenuItem> items;
    private final HashBiMap<Integer, MenuItem> interacts;
    private final Menu holder;

    public MenuPage(Menu holder, String name, int size, int pagenumber, Decoration decoration) {
        this.holder = holder;
        items = HashBiMap.create(60);
        interacts = HashBiMap.create(60);
        inv = Bukkit.createInventory(null, size, MessageUtil.convertMsg(name));
        this.pagenumber = pagenumber;
        decorationSlots = new HashSet<>();
        setDecoration(decoration);
        holder.plugin.getServer().getPluginManager().registerEvents(this, holder.plugin);
    }

    public MenuPage(Menu holder, String name, int size, int pagenumber, Decoration decoration, String unicode) {
        this.holder = holder;
        items = HashBiMap.create(60);
        interacts = HashBiMap.create(60);

        String trueName = "\uF818\uF811\uF831<white>" + unicode + "\uF81C\uF81A\uF818\uF814<reset>" + name;

        inv = Bukkit.createInventory(null, size, MessageUtil.convertMsg(trueName));
        this.pagenumber = pagenumber;
        decorationSlots = new HashSet<>();
        setDecoration(decoration);
        holder.plugin.getServer().getPluginManager().registerEvents(this, holder.plugin);
    }

    // you can get the inventory with this
    public Inventory getInventory() {
        return inv;
    }

    public int getPagenumber() {
        return pagenumber;
    }

    public Menu getHolder() {
        return holder;
    }

    public void setItem(int slot, MenuItem mItem) {
        items.put(slot, mItem);
        inv.setItem(slot, mItem.getItem());
        mItem.setPage(this);
        mItem.setSlot(slot);
    }

    public void setItems(Map<Integer, MenuItem> mItems) {
        for (Integer slot : mItems.keySet()) {
            setItem(slot, mItems.get(slot));
        }
    }

    public void updateItem(MenuItem mItem) {
        inv.setItem(mItem.getSlot(), mItem.getItem());
    }

    public void updateItem(int slot) {
        MenuItem mItem = items.get(slot);
        updateItem(mItem);
    }


    public void updateAllItems() {
        for (MenuItem mItem : items.inverse().keySet()) {
            updateItem(mItem);
        }
    }

    public MenuItem getItem(int slot) {
        return items.get(slot);
    }

    public void removeItem(int slot) {
        items.remove(slot);
        inv.setItem(slot, new ItemStack(Material.AIR));
    }

    public void removeItem(MenuItem mItem) {
        int slot = mItem.getSlot();
        removeItem(slot);
    }

    public void addInteract(int slot, MenuItem interact) {
        interacts.put(slot, interact);
        interact.setPage(this);
        interact.setSlot(slot);
    }

    public void addInteracts(Map<Integer, MenuItem> interact) {
        for (Integer slot : interact.keySet()) {
            addInteract(slot, interact.get(slot));
        }
    }

    public void setInteracts() {
        for (Integer slot : interacts.keySet()) {
            setInteract(slot);
        }
    }

    public void setInteract(int slot) {
        MenuItem interact = interacts.get(slot);
        inv.setItem(slot, interact.getItem());
        interact.setPage(this);
        interact.setSlot(slot);
    }

    public void setInteract(MenuItem interact) {
        int slot = interacts.inverse().get(interact);
        setInteract(slot);
    }

    private void setDecoration(Decoration decoration) {
        ItemStack[] itemMap = decoration.getItemMap();
        inv.setContents(itemMap);
        for (int i = 0; i < itemMap.length; i++) {
            if (!itemMap[i].getType().equals(Material.AIR)) {
                decorationSlots.add(i);
            }
        }
    }

    public BiMap<Integer, MenuItem> getItems() {
        return items;
    }

    public BiMap<Integer, MenuItem> getInteracts() {
        return interacts;
    }

    public Set<Integer> getDecorationSlots() {
        return decorationSlots;
    }

    // You can open the inventory with this
    public void openInventory(Player p) {
        p.openInventory(inv);
    }

    // You can close the inventory with this
    public void closeInventory(Player p) {
        p.closeInventory();
    }


    // Cancel any drag event in a menu. Currently there is no use for them here.
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!event.getInventory().equals(getInventory())) return;
        if (!event.getInventorySlots().containsAll(event.getRawSlots())) return;
        event.setCancelled(true);
    }

    // This event will return any interact items the player left in the menu when closing it.
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if (!event.getInventory().equals(getInventory())) return;
        // If we get here we need to trigger the MenuCloseEvent before moving on.
        MenuCloseEvent mce = new MenuCloseEvent((Player) event.getPlayer(), this);
        mce.callEvent();

        if (interacts.isEmpty()) return;
        for (int slot : interacts.keySet()) {
            // If the item in the slot is not one that started there, refund it
            ItemStack invItem = inv.getItem(slot);
            MenuItem interact = interacts.get(slot);
            if (invItem == null) continue;
            if (interact == null) continue;
            ItemStack interactItem = interact.getItem();
            if (invItem.isSimilar(interactItem)) continue;
            PlayerUtil.giveOrDropItem(player, invItem);
        }
    }

    // Check for clicks on items
    // NOTE: We cancel the event when we don't want an item to move. 
    //       Only items we want moving should result in no cancelling
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getInventory().equals(getInventory())) return;

        ItemStack clickItem = event.getCurrentItem();

        // verify current item is not null
        if (clickItem == null || clickItem.getType() == Material.AIR) return;

        ClickType click = event.getClick();
        if (click.isKeyboardClick()) {
            event.setCancelled(true);
        }

        // Cancel the event if this is not an interact menu.
        // Interact menus have many more rules to handle cursor items.
        if (!interacts.isEmpty()) {

            // If we have an interact slot the menu will allow the player to shift+click without canceling
            // We handle that here by canceling the shift+click and directing it towards an interact slot.
            if (click.isShiftClick()) {
                // For Top inventory, only cancel if it's not an interact slot
                // For Bottom, we need to create special rules to send items only to interact slots. So cancel immediately
                if (isTopInventory(event)) {
                    if (!isSlotInteractive(event)) event.setCancelled(true);
                } else {
                    // Two main behaviors:
                    // 1) Empty interact slot? Fill it
                    // 2) Half full interact slot with same item? Fill it and use left over to fill next slot if available
                    for (int slot : interacts.keySet()) {
                        ItemStack slotItem = inv.getItem(slot);

                        // 1)
                        if (slotItem == null || slotItem.getType().equals(Material.AIR)) {
                            inv.setItem(slot, clickItem);
                            clickItem.setAmount(0);
                            break;
                            // 2)
                        } else if (slotItem.isSimilar(clickItem)) {
                            // If we can do a clean add, great, do it.
                            // If not, add what we can and go on to next interact slot
                            if (slotItem.getAmount() + clickItem.getAmount() <= slotItem.getMaxStackSize()) {
                                slotItem.setAmount(slotItem.getAmount() + clickItem.getAmount());
                                clickItem.setAmount(0);
                                break;
                            } else if (slotItem.getAmount() < slotItem.getMaxStackSize()) {
                                clickItem.setAmount(clickItem.getAmount() - (slotItem.getMaxStackSize() - slotItem.getAmount()));
                                slotItem.setAmount(slotItem.getMaxStackSize());
                            }
                        }
                    }
                    event.setCancelled(true);
                }
            }

            //Cancel the even if the top inventory was clicked AND
            //the item clicked is not in an interact slot
            if (isTopInventory(event) && !isSlotInteractive(event)) {
                event.setCancelled(true);
            }
        } else {
            event.setCancelled(true);
            // This acts as extra insurance to prevent players from taking items.
            holder.plugin.getServer().getScheduler().runTaskLater(holder.plugin, () -> {
                Player player = ((Player) event.getWhoClicked());
                if (player.getItemOnCursor().equals(clickItem)) {
                    event.getInventory().setItem(event.getRawSlot(), clickItem);
                    player.setItemOnCursor(null);
                }
                player.updateInventory();
            }, 1);
        }

        if (!(event.getWhoClicked() instanceof Player clicker)) return;
        int slot = event.getRawSlot();
        ClickSound clicksound = holder.getClickSound();
        if (clicksound.hasSound() && items.containsKey(slot)) {
            clicker.playSound(clicker.getLocation(), clicksound.getSound(), clicksound.getVolume(), clicksound.getPitch());
        }
        holder.handleItemClick(this, clickItem, slot, clicker, event);

    }

    private boolean isSlotInteractive(InventoryClickEvent event) {
        return interacts.containsKey(event.getRawSlot());
    }

    private boolean isTopInventory(InventoryClickEvent event) {
        return event.getRawSlot() == event.getSlot();
    }
}



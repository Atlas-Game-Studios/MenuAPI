package com.ags.menuapi.Menu;

import com.ags.menuapi.MenuItem.MenuItem;
import com.ags.menuapi.addons.*;
import com.ags.menuapi.decoration.Decoration;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Menu {

    public JavaPlugin plugin;

    public String name;
    public String unicode;
    public int size;
    public MenuSize menusize;
    public List<MenuPage> pages;

    private ClickSound clickSound;

    public boolean overrideDecoration = true;
    public Decoration decoration;

    private BackButton backButton;
    private NextButton nextButton;
    private PrevButton prevButton;

    private final List<PermButton> permButtons = new ArrayList<PermButton>();

    public MenuSize getMenuSize() {
        return menusize;
    }

    public int getSize() {
        return size;
    }

    public ClickSound getClickSound() {
        if (clickSound == null) {
            clickSound = new ClickSound(false);
        }
        return clickSound;
    }

    public void setClickSound() {
        clickSound = new ClickSound();
    }

    public void setClickSound(Sound sound) {
        clickSound = new ClickSound();
        clickSound.setClickSound(sound);
    }

    public NextButton getNextButton() {
        if (nextButton == null) {
            nextButton = new NextButton(this);
        }
        return nextButton;
    }

    public PrevButton getPrevButton() {
        if (prevButton == null) {
            prevButton = new PrevButton(this);
        }
        return prevButton;
    }

    public BackButton getBackButton() {
        if (backButton == null) {
            backButton = new BackButton(this);
        }
        return backButton;
    }

    public void setBackMenu(Menu backMenu) {
        backButton = new BackButton(this, backMenu);
        for (MenuPage page : pages) {
            page.setItem(backButton.getSlot(), backButton.getItem());
        }
    }

    /**
     * getLastPage() - This will return a reference to the
     *
     * @return Last MenuPage in the menu
     */
    public MenuPage getLastPage() {
        if (pages.size() == 0) {
            return null;
        }
        return pages.get(pages.size() - 1);
    }

    /**
     * addItemToPage() - This method adds a MenuItem in a specific slot on a page in a menu
     * Remember to use the finish() function to finish a menu after adding all MenuItems to it.
     * <br>NOTE: Items put in slots that go beyond the size of this menu will default to the last slot.
     * <br>NOTE: If making a CallbackMenu the MenuItemAction for MenuItems can be ignored. This is only used in an Action Menu
     *
     * @param page  - The page to add this MenuItem to.
     * @param slot  - The slot on the page to add the MenuItem to.
     * @param mItem - The MenuItem to add.
     */
    public void addItemToPage(int page, int slot, MenuItem mItem) {
        pages.get(page).setItem(slot, mItem);
    }

    /**
     * addItemToAllPages() - This method adds a MenuItem in a specific slot on all pages in a menu
     * Remember to use the finish() function to finish a menu after adding all MenuItems to it.
     * <br>NOTE: Items put in slots that go beyond the size of this menu will default to the last slot.
     * <br>NOTE: If making a CallbackMenu the MenuItemAction for MenuItems can be ignored. This is only used in an Action Menu
     *
     * @param slot  - The slot on every page to add the MenuItem to.
     * @param mItem - The MenuItem to add.
     */
    public void addItemToAllPages(int slot, MenuItem mItem) {
        for (MenuPage page : pages) {
            page.setItem(slot, mItem);
        }
        permButtons.add(new PermButton(slot, mItem));
    }

    /**
     * addItemsToPage() - This method adds multiple MenuItems to a page in a menu
     * Remember to use the finish() function to finish a menu after adding all MenuItems to it.
     * <br>NOTE: Items put in slots that go beyond the size of this menu will default to the last slot.
     * <br>NOTE: If making a CallbackMenu the MenuItemAction for MenuItems can be ignored. This is only used in an Action Menu
     *
     * @param page   - The page to add this MenuItem to.
     * @param mItems - A HashMap<Integer,MenuItem> corresponding to multiple (slot,MenuItem) pairs to add to the menu.
     */
    public void addItemsToPage(int page, Map<Integer, MenuItem> mItems) {
        MenuPage menupage = pages.get(page);
        menupage.setItems(mItems);
    }

    /**
     * getItemOnPage() - This method returns the MenuItem on a slot on a specific page.
     *
     * @param page - The page on which to look at the slot for a MenuItem
     * @param slot - The slot to look for an item in.
     * @return The MenuItem in the slot given. Can return null.
     */
    public MenuItem getItemOnPage(int page, int slot) {
        return pages.get(page).getItem(slot);
    }

    /**
     * updatePage() - This method updates the displayed settings of all items on a page
     * This is the lazy way to update an entire page. For maximum efficiency update each
     * Item individually if it actually needs an update.
     *
     * @param page
     */
    public void updatePage(int page) {
        pages.get(page).updateAllItems();
    }

    /**
     * updateItem() - This method updates a single Item on the page. As with it's counterpart
     * updateItem(mItem) update as fast as possible. Menus rely on a Bidirectional Hashmap to
     * assign 1:1 values for slots and items. This means updates happen equally as quickly
     * despite what is given.
     *
     * @param page - The page the item exists on.
     * @param slot - The slot where the item can be found.
     */
    public void updateItem(int page, int slot) {
        pages.get(page).updateItem(slot);
    }

    /**
     * updateItem() - This method updates a single Item on the page. As with it's counterpart
     * updateItem(slot) update as fast as possible. Menus rely on a Bidirectional Hashmap to
     * assign 1:1 values for slots and items. This means updates happen equally as quickly
     * despite what is given.
     *
     * @param mItem - The item to update
     */
    public void updateItem(MenuItem mItem) {
        mItem.getPage().updateItem(mItem);
    }

    /**
     * updateItemName() - This method allows items to be updated while the menu is open.
     * This method also can set the display name of the specific item. To simply update
     * the item or the page it's on use updatePage() or updateItem().
     *
     * @param mItem - The item to update.
     * @param name  - The new display name to set.
     */
    public void updateItemName(MenuItem mItem, String name) {
        mItem.setName(name);
        mItem.getPage().updateItem(mItem);
    }

    /**
     * updateItemName() - This method allows items to be updated while the menu is open.
     * This method also can set the display name of the specific item. To simply update
     * the item or the page it's on use updatePage() or updateItem().
     *
     * @param mItem  - The item to update.
     * @param amount - The amount of the item
     */
    public void updateItemAmount(MenuItem mItem, int amount) {
        mItem.setAmount(amount);
        mItem.getPage().updateItem(mItem);
    }

    /**
     * updateItemDesc() - This method allows items to be updated while the menu is open.
     * This method also can set the description of the specific item. To simply update
     * the item or the page it's on use updatePage() or updateItem().
     *
     * @param mItem - The item to update.
     * @param lines - The new description to set.
     */
    public void updateItemDesc(MenuItem mItem, List<String> lines) {
        mItem.setDesc(lines);
        mItem.getPage().updateItem(mItem);
    }

    /**
     * updateItemHighligh() - This method allows items to be updated while the menu is open.
     * This method also can toggle the highlight on the specific item. To simply update
     * the item or the page it's on use updatePage() or updateItem().
     *
     * @param mItem - The item to update.
     */
    public void updateItemHighligh(MenuItem mItem) {
        if (mItem.isHighlighted()) mItem.unhighlight();
        else mItem.highlight();
        mItem.getPage().updateItem(mItem);
    }

    /**
     * removeItem() - This method removes an item from an active menu.
     * The item will immediately disappear to all viewers.
     *
     * @param page - The page the item is on
     * @param slot - The slot the item is in
     */
    public void removeItem(int page, int slot) {
        pages.get(page).removeItem(slot);
    }

    public void removeItem(MenuItem mItem) {
        mItem.getPage().removeItem(mItem);
    }

    /**
     * getPage() - Returns the MenuPage of this menu.
     *
     * @param page - The number of the page to retrieve.
     * @return The MenuPage and all it's info. Can return null if page does not exist.
     */
    public MenuPage getPage(int page) {
        if (pages.size() > page) {
            return pages.get(page);
        }
        return null;
    }

    /**
     * addPage() - Adds a page to this menu.
     */
    public void addPage() {
        MenuPage page;
        if (unicode == null) page = new MenuPage(this, name, size, pages.size(), decoration);
        else page = new MenuPage(this, name, size, pages.size(), decoration, unicode);

        pages.add(page);
        int pageIndex = pages.indexOf(page);

        // Set the backbutton if it is active
        if (getBackButton().isActive()) {
            page.setItem(getBackButton().getSlot(), getBackButton().getItem());
        }

        // Check if there is a page after this one
        if (pageIndex < pages.size() - 1) {
            page.setItem(getNextButton().getSlot(), getNextButton().getItem()); // Set next button on current page
            MenuPage nextPage = pages.get(pageIndex + 1);
            nextPage.setItem(getPrevButton().getSlot(), getPrevButton().getItem()); // Set prev button on next page.
        }

        // check if there is a page before this one.
        if (pageIndex > 0) {
            page.setItem(getPrevButton().getSlot(), getPrevButton().getItem()); // Set prev button on current page
            MenuPage prevPage = pages.get(pageIndex - 1);
            prevPage.setItem(getNextButton().getSlot(), getNextButton().getItem()); // Set next button on prev page.

        }

        // Set any perm button created on this page.
        for (PermButton permButton : permButtons) {
            page.setItem(permButton.getSlot(), permButton.getItem());
        }

    }

    /**
     * open() - Opens the first page of the Menu for the player specified. This will close
     * any other open menu. If switching menus use this and never close()
     *
     * @param player - The player to open the Menu for.
     */
    public void open(Player player) {
        pages.get(0).openInventory(player);
    }

    /**
     * open() - Opens the made Menu to the specified page for the player specified.
     * This will close any other open menu. If switching menus use this and never close()
     *
     * @param player - The player to open the Menu for.
     * @param page   - The page of the menu to open.
     */
    public void open(Player player, int page) {
        pages.get(page).openInventory(player);
    }

    /**
     * close() - close the current open menu for the player
     * <br>NOTE: This will close ANY open menu, not only this one.
     *
     * @param player - The player to close the menu for.
     */
    public void close(Player player) {
        player.closeInventory();
    }

    /**
     * handleItemClick() - This will simulate a click of a MenuItem and trigger its action or call the callback function
     *
     * @param page    - The page that the click happened on.
     * @param item    - The item that was clicked.
     * @param slot    - The slot that was clicked.
     * @param clicker - The person who clicked.
     * @param event   - The InventoryClickEvent.
     */
    public void handleItemClick(MenuPage page, ItemStack item, int slot, Player clicker, InventoryClickEvent event) {
        if (slot == getBackButton().getSlot()) {
            if (getBackButton().isActive()) {
                getBackButton().getBackMenu().open(clicker);
            }
        } else if (slot == getNextButton().getSlot()) {
            if (pages.indexOf(page) < pages.size() - 1) {
                pages.get(pages.indexOf(page) + 1).openInventory(clicker);
            }
        } else if (slot == getPrevButton().getSlot()) {
            if (pages.indexOf(page) > 0) {
                pages.get(pages.indexOf(page) - 1).openInventory(clicker);
            }
        }

    }


}

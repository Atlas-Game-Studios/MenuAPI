package com.ags.menuapi.test;

import com.ags.menuapi.Menu.CallbackMenu;
import com.ags.menuapi.Menu.Menu;
import com.ags.menuapi.Menu.MenuPage;
import com.ags.menuapi.Menu.MenuSize;
import com.ags.menuapi.MenuAPI;
import com.ags.menuapi.MenuItem.MenuItem;
import com.ags.menuapi.callbacks.ClickCallback;
import com.ags.menuapi.decoration.Decoration;
import com.ags.menuapi.decoration.Scheme;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Test implements CommandExecutor {

    MenuAPI plugin;

    FlashingMenuTest game;

    public Test(MenuAPI plugin) {
        this.plugin = plugin;
        game = new FlashingMenuTest(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("menuAPI") && sender.hasPermission("menuapi.admin")) {
            if (args[0].equalsIgnoreCase("test")) {
                testCallbackMenu(sender);
            } else if (args[0].equalsIgnoreCase("action")) {
                testActionMenu(sender);
            } else if (args[0].equalsIgnoreCase("game")) {
                if (args.length == 2) {
                    game.open((Player) sender, Integer.valueOf(args[1]));
                } else {
                    game.open((Player) sender, 16);
                }

            }
            return true;
        }
        return false;
    }

    private void testCallbackMenu(CommandSender sender) {

        // Make Fish Counting Menu
        // Make Handler for Menu
        // This is the code that runs whenever your menu is clicked in or used.
        // This is where you check the slot to know what was clicked and then preform the needed actions.
        // This specific handler checks you've clicked slot 10 which then counts up all fish in slots (11-16)
        ClickCallback fishCountHandler = new ClickCallback() {
            public void callback(Menu menu, MenuPage page, ItemStack item, int slot, Player clicker, InventoryClickEvent event) {
                Inventory inv = event.getClickedInventory();
                int fishTotal = 0;
                if (slot == 10) {
                    for (int i = 11; i < 17; i++) {
                        if (inv.getItem(i) != null && inv.getItem(i).getType().equals(Material.COD)) {
                            fishTotal += inv.getItem(i).getAmount();
                        }
                    }
                    clicker.sendMessage(ChatColor.GRAY + "You have " + fishTotal + " Fish");
                }
            }
        };

        // Make Decorations (Can be Scheme.EMPTY for no decorations)
        // Decorations are unclickable and do nothing at all. They just look nice. You choose a material and scheme.
        // You can create a custom scheme which is simply a list of slot numbers to fill with the given material.
        Decoration decoration = new Decoration(MenuSize.MEDIUM, Material.BLACK_STAINED_GLASS_PANE, Scheme.FILL);

        // Make Menu
        // (Reference your plugin, name of menu, size of menu, menu pages, menu callback handler, decorations)
        CallbackMenu fishCounter = MenuAPI.createCallbackMenu(plugin, "Fish Counter", MenuSize.MEDIUM, 1, decoration);
        fishCounter.setCallbackHandler(fishCountHandler);
        // Add Page Items to Menu
        // (page number, slot number, MenuItem(Itemstack, item name, lore lines))
        fishCounter.addItemToPage(0, 10, new MenuItem(new ItemStack(Material.FISHING_ROD), "Count Fish", "Click here to count", "all your fish"));

        // Add Interactable Page slots to Menu (Optional)
        // Interacts are special slots players can put items in.
        // They can then be referenced in your handler like in this exampel which counts fish.
        for (int i = 11; i < 17; i++) {
            fishCounter.addInteractToPage(0, i, new MenuItem(new ItemStack(Material.AIR)));
        }

        // Make Ore Counting Menu
        // Make Handler for Menu
        ClickCallback oreCountHandler = new ClickCallback() {
            public void callback(Menu menu, MenuPage page, ItemStack item, int slot, Player clicker, InventoryClickEvent event) {
                Inventory inv = event.getClickedInventory();
                int fishTotal = 0;
                if (slot == 10) {
                    for (int i = 11; i < 17; i++) {
                        if (inv.getItem(i) != null && inv.getItem(i).getType().equals(Material.DIAMOND)) {
                            fishTotal += inv.getItem(i).getAmount();
                        }
                    }
                    clicker.sendMessage(ChatColor.GRAY + "You have " + fishTotal + " Ore");
                }


            }
        };

        // Make Menu
        CallbackMenu oreCounter = MenuAPI.createCallbackMenu(plugin, "Fish Counter", MenuSize.MEDIUM, 1, decoration);
        oreCounter.setCallbackHandler(oreCountHandler);
        // Add Page Items to Menu
        oreCounter.addItemToPage(0, 10, new MenuItem(new ItemStack(Material.DIAMOND_PICKAXE), "Count Ore", "Click here to count", "all your ore"));

        // Add Interactable Page slots to Menu (Optional)
        for (int i = 11; i < 17; i++) {
            oreCounter.addInteractToPage(0, i, new MenuItem(new ItemStack(Material.AIR)));
        }


        // Make Main Menu
        // Make Handler for Menu
        // The only real difference between this menu and the last ones, is that we plan to link both the counter menus to this one.
        // That way, both counting menus will have back buttons that lead back to the main menu
        ClickCallback mainHandler = new ClickCallback() {
            public void callback(Menu menu, MenuPage page, ItemStack item, int slot, Player clicker, InventoryClickEvent event) {
                if (slot == 4) {
                    clicker.sendMessage("Welcome to the Counter Menu!");
                }

                if (slot == 10) {
                    if (page.getPagenumber() == 0) {
                        fishCounter.open(clicker);
                    } else if (page.getPagenumber() == 1) {
                        oreCounter.open(clicker);
                    }
                }
            }
        };

        // Make Decorations
        Decoration decorationMain = new Decoration(MenuSize.MEDIUM, Material.BLACK_STAINED_GLASS_PANE, Scheme.TOPBOTTOM);

        // Make Menu
        CallbackMenu mainmenu = MenuAPI.createCallbackMenu(plugin, "Counter Menu", MenuSize.MEDIUM, 2, decorationMain);
        mainmenu.setCallbackHandler(mainHandler);
        // Add Page Items to Menu
        // Worth noting that you can add items to all pages or only specific pages
        mainmenu.addItemToAllPages(4, new MenuItem(new ItemStack(Material.OAK_SIGN), "Atlas Menu", "This is a Parent Menu"));
        mainmenu.addItemToPage(0, 10, new MenuItem(new ItemStack(Material.FISHING_ROD), "Fish Counter", "Go Here to count", "All your fish"));
        mainmenu.addItemToPage(1, 10, new MenuItem(new ItemStack(Material.DIAMOND_PICKAXE), "Ore Counter", "Go Here to count", "All your diamonds"));


        // Set Parent menu for the Back button to work (Optional)
        // This is where we link this newly made main menu to both it's sub menus.
        // This is done be setting the parent menu for both sub menus.
        // Menus only keep track of their parent. Not the other way around.
        fishCounter.setBackMenu(mainmenu);
        oreCounter.setBackMenu(mainmenu);

        // Open a menu
        mainmenu.open((Player) sender);
    }

    private void testActionMenu(CommandSender sender) {
        Decoration decoration = new Decoration(MenuSize.MEDIUM, Material.GREEN_STAINED_GLASS_PANE, Scheme.TOPBOTTOM);
        Menu menu = MenuAPI.createActionMenu(plugin, "Test", MenuSize.MEDIUM, 1, decoration);
        menu.addItemToPage(0, 9, new MenuItem(
                (MenuPage page, Player clicker, InventoryClickEvent event) -> {
                    clicker.sendMessage("It works: Action!");
                }, new ItemStack(Material.ACACIA_BOAT), "Test Item", "Line 1", "Line2", "Line3", "Line4"));
        menu.open((Player) sender);


    }
}

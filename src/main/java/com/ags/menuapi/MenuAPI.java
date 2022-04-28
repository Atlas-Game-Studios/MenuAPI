package com.ags.menuapi;

import com.ags.menuapi.Menu.*;
import com.ags.menuapi.MenuItem.MenuItem;
import com.ags.menuapi.addons.BackButton;
import com.ags.menuapi.addons.NextButton;
import com.ags.menuapi.addons.PrevButton;
import com.ags.menuapi.decoration.Decoration;
import com.ags.menuapi.test.Test;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MenuAPI extends JavaPlugin {

    public static List<Menu> menus = new ArrayList<>();

    private static MenuItem backButtonItem;
    private static MenuItem nextButtonItem;
    private static MenuItem prevButtonItem;

    private static HashMap<MenuSize, Integer> backButtonSlot;
    private static HashMap<MenuSize, Integer> nextButtonSlot;
    private static HashMap<MenuSize, Integer> prevButtonSlot;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        Server server = getServer();
        ConsoleCommandSender console = server.getConsoleSender();
        console.sendMessage(ChatColor.BLUE + "[" + this.getName() + "]" + ChatColor.WHITE + " ---------------------------------");
        console.sendMessage(ChatColor.BLUE + "[" + this.getName() + "]" + ChatColor.WHITE + " Plugin has been enabled");
        console.sendMessage(ChatColor.BLUE + "[" + this.getName() + "]" + ChatColor.WHITE + " ---------------------------------");
        console.sendMessage(ChatColor.BLUE + "[" + this.getName() + "]" + ChatColor.WHITE + " Version: " + this.getDescription().getVersion());
        console.sendMessage(ChatColor.BLUE + "[" + this.getName() + "]" + ChatColor.WHITE + " ---------------------------------");

        loadDefaults();

        getCommand("menuAPI").setExecutor(new Test(this));
    }

    @Override
    public void onDisable() {
        Server server = getServer();
        ConsoleCommandSender console = server.getConsoleSender();
        console.sendMessage(ChatColor.BLUE + "[" + this.getName() + "]" + ChatColor.WHITE + " ---------------------------------");
        console.sendMessage(ChatColor.BLUE + "[" + this.getName() + "]" + ChatColor.WHITE + " Plugin has been disabled");
        console.sendMessage(ChatColor.BLUE + "[" + this.getName() + "]" + ChatColor.WHITE + " ---------------------------------");
    }

    private void loadDefaults() {

        backButtonItem = getButton("BackButtonItem");
        nextButtonItem = getButton("NextButtonItem");
        prevButtonItem = getButton("PrevButtonItem");
        backButtonSlot = getSlots("BackButtonSlot");
        nextButtonSlot = getSlots("NextButtonSlot");
        prevButtonSlot = getSlots("PrevButtonSlot");
        setMenuDefaults();
    }

    private static void setMenuDefaults() {
        BackButton.load(backButtonSlot, backButtonItem);
        NextButton.load(nextButtonSlot, nextButtonItem);
        PrevButton.load(prevButtonSlot, prevButtonItem);
    }

    private HashMap<MenuSize, Integer> getSlots(String id) {
        FileConfiguration config = this.getConfig();
        HashMap<MenuSize, Integer> slotmap = new HashMap<MenuSize, Integer>();
        slotmap.put(MenuSize.NINE, config.getInt(id + ".9"));
        slotmap.put(MenuSize.ONEEIGHT, config.getInt(id + ".18"));
        slotmap.put(MenuSize.TWOSEVEN, config.getInt(id + ".27"));
        slotmap.put(MenuSize.THREESIX, config.getInt(id + ".36"));
        slotmap.put(MenuSize.FOURFIVE, config.getInt(id + ".45"));
        slotmap.put(MenuSize.FIVEFOUR, config.getInt(id + ".54"));
        slotmap.put(MenuSize.TINY, config.getInt(id + ".9"));
        slotmap.put(MenuSize.SMALL, config.getInt(id + ".18"));
        slotmap.put(MenuSize.MEDIUM, config.getInt(id + ".27"));
        slotmap.put(MenuSize.LARGE, config.getInt(id + ".36"));
        slotmap.put(MenuSize.HUGE, config.getInt(id + ".45"));
        slotmap.put(MenuSize.GIGANTIC, config.getInt(id + ".54"));
        return slotmap;
    }

    private MenuItem getButton(String id) {
        FileConfiguration config = this.getConfig();
        Material mat = Material.getMaterial(config.getString(id + ".Material"));
        String name = ChatColor.translateAlternateColorCodes('&', config.getString(id + ".Name"));
        ArrayList<String> lore = new ArrayList<String>();
        for (String loreline : config.getStringList(id + ".Lore")) {
            lore.add(ChatColor.translateAlternateColorCodes('&', loreline));
        }
        if (mat != null && name != null && lore != null) {
            return new MenuItem(new ItemStack(mat), name, lore);
        }
        Bukkit.getLogger().info(ChatColor.RED + "[MenuAPI][ERROR]: Incorrect Button Config for: " + id);
        return null;
    }


    /**
     * Callback Menu - A Menu that requires a callback function. Specifically a MenuCallback. This function
     * can be set later or in an alternate constructor. It must implement MenuCallback and it's singular
     * callback() function. All menus should also have a Decoration Scheme and MenuItems to be functional.
     * These Decorations and MenuItems can be set later or in an alternate constructor.
     *
     * @param name  - The name of the overall Menu. You can set individual menu page names later.
     * @param size  - The size of the menu. This is an enum to enforce proper sizing. Use MenuSize to set this
     * @param pages - The number of pages for this menu.
     * @return - Returns the constructed menu. From here you must set the callback function, the decoration scheme, and the menu options.
     */
    public static CallbackMenu createCallbackMenu(JavaPlugin plugin, String name, MenuSize size, int pages, Decoration decoration) {
        CallbackMenu menu = new CallbackMenu(plugin, name, size, pages, decoration);
        return menu;
    }


    /**
     * Animated Menu - A Menu that has an animation function that runs every specified interval.
     * It must implement MenuCallback and it's singular callback() function.
     * It must implement AnimationCallback and it's singular callback() function.
     * This menu also has a Decoration Scheme. This is made through creating a new Decoration. All menus should
     * also have MenuItems to be functional. These MenuItems can be set later or in an alternate constructor.
     *
     * @param name           - The name of the overall Menu. You can set individual menu page names later.
     * @param size           - The size of the menu. This is an enum to enforce proper sizing. Use MenuSize to set this
     * @param pages          - The number of pages for this menu.
     * @param decoration     - The Decoration scheme to use for this menu and all it's pages.
     * @param animationSpeed - The speed in ticks for the menu to update at.
     * @return Returns the constructed menu. From here you must set the menu options.
     */
    public static AnimatedMenu createAnimatedMenu(JavaPlugin plugin, String name, MenuSize size, Decoration decoration, int animationSpeed) {
        AnimatedMenu menu = new AnimatedMenu(plugin, name, size, decoration, animationSpeed);
        return menu;
    }

    /**
     * Game Menu - A Menu that has an animation function that runs every specified interval as well as it's own list of variables.
     * for storring information for a minigame that exists inside the menu.
     * It must implement MenuCallback and it's singular callback() function.
     * It must implement AnimationCallback and it's singular callback() function.
     * This menu also has a Decoration Scheme. This is made through creating a new Decoration. All menus should
     * also have MenuItems to be functional. These MenuItems can be set later or in an alternate constructor.
     *
     * @param name           - The name of the overall Menu. You can set individual menu page names later.
     * @param size           - The size of the menu. This is an enum to enforce proper sizing. Use MenuSize to set this
     * @param pages          - The number of pages for this menu.
     * @param decoration     - The Decoration scheme to use for this menu and all it's pages.
     * @param animationSpeed - The speed in ticks for the menu to update at.
     * @return Returns the constructed menu. From here you must set the menu options.
     */
    public static GameMenu createGameMenu(JavaPlugin plugin, String name, MenuSize size, Decoration decoration, int animationSpeed) {
        GameMenu menu = new GameMenu(plugin, name, size, decoration, animationSpeed);
        return menu;
    }


    /**
     * Action Menu - A Menu that has actions built into each MenuItem.
     * This menu also has a Decoration Scheme. This is made through creating a new Decoration. All menus should
     * also have MenuItems to be functional. These MenuItems can be set later or in an alternate constructor.
     *
     * @param name       - The name of the overall Menu. You can set individual menu page names later.
     * @param size       - The size of the menu. This is an enum to enforce proper sizing. Use MenuSize to set this
     * @param pages      - The number of pages for this menu.
     * @param decoration - The Decoration scheme to use for this menu and all it's pages.
     * @return Returns the constructed menu. From here you must set the menu options.
     */
    public static ActionMenu createActionMenu(JavaPlugin plugin, String name, MenuSize size, int pages, Decoration decoration) {
        ActionMenu menu = new ActionMenu(plugin, name, size, pages, decoration);
        return menu;
    }

    /**
     * List Menu - A Menu that extends Action menus with specific list functionality.
     * This menu also has a Decoration Scheme. This is made through creating a new Decoration. All menus should
     * also have MenuItems to be functional. These MenuItems can be set later or in an alternate constructor.
     *
     * @param name       - The name of the overall Menu. You can set individual menu page names later.
     * @param size       - The size of the menu. This is an enum to enforce proper sizing. Use MenuSize to set this
     * @param pages      - The number of pages for this menu.
     * @param decoration - The Decoration scheme to use for this menu and all it's pages.
     * @return Returns the constructed menu. From here you must set the menu options. F
     */
    public static ListMenu createListMenu(JavaPlugin plugin, String name, MenuSize size, int pages, Decoration decoration) {
        ListMenu menu = new ListMenu(plugin, name, size, pages, decoration);
        return menu;
    }


    /**
     * List Menu -  A Menu that extends Action menus with specific list functionality.
     * All menus should also have a Decoration Scheme and MenuItems to be functional. These Decorations and
     * MenuItems can be set later or in an alternate constructor.
     *
     * @param name  - The name of the overall Menu. You can set individual menu page names later.
     * @param size  - The size of the menu. This is an enum to enforce proper sizing. Use MenuSize to set this
     * @param pages - The number of pages for this menu.
     * @param min   - The min slot number on a page that list items can appear on.
     * @param max   - the max slot number on a page that list items can appear on
     * @return Returns the constructed menu. From here you must set the decoration scheme, and the menu options.
     */
    public static ListMenu createListMenu(JavaPlugin plugin, String name, MenuSize size, int pages, Decoration decoration, int min, int max) {
        ListMenu menu = new ListMenu(plugin, name, size, pages, decoration, min, max);
        return menu;
    }

    /**
     * cahceMenu() - Cache this menu in the MenuAPI for faster reference and to make sure it doesn't disappear.
     * Creating menus constantly can start to slow down the server, caching them can help prevent that. You can then
     * find cached menus by using the getMenu() function and finding it from it's name.
     * <br> NOTE: All cached menus must have unique names.
     *
     * @param menu
     */
    public static void cacheMenu(Menu menu) {
        for (Menu savedMenu : menus) {
            if (savedMenu.name.equalsIgnoreCase(menu.name)) {
                Bukkit.getLogger().info(ChatColor.RED + "[MenuAPI][ERROR]: Menu cached with that name already! Menu not cached.");
            } else {
                menus.add(menu);
            }
        }
    }

    /**
     * getMenu() - Get a cached Menu from it's given name
     *
     * @param name - The name of the cached Menu to return
     * @return The cached Menu with the name supplied.
     */
    public static Menu getMenu(String name) {
        for (Menu savedMenu : menus) {
            if (savedMenu.name.equalsIgnoreCase(name)) {
                return savedMenu;
            }
        }
        return null;
    }

    /**
     * unchacheMenu() - This will un-chache a menu from the MenuAPI list. This should only be neeesary
     * if you plan to add a new menu to the cachce with the same name. Otherwise RAM usage from caching
     * should not cause any issues.
     *
     * @param menu - The menu to remove from the cache.
     */
    public static void unchacheMenu(Menu menu) {
        menus.remove(menu);
    }

    public static void openMenu(MenuPage menu, Player player) {
        menu.openInventory(player);
    }

    public static void closeMenu(MenuPage menu, Player player) {
        menu.closeInventory(player);
    }
}

package com.ags.menuapi.test;

import com.ags.menuapi.Menu.*;
import com.ags.menuapi.MenuAPI;
import com.ags.menuapi.MenuItem.MenuItem;
import com.ags.menuapi.callbacks.AnimationCallback;
import com.ags.menuapi.callbacks.ClickCallback;
import com.ags.menuapi.decoration.Decoration;
import com.ags.menuapi.decoration.Scheme;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static net.md_5.bungee.api.ChatColor.*;

public class FlashingMenuTest {

    MenuAPI plugin;
    MenuSize size;

    String START = "start";
    String ITEMS = "items";
    String TIME = "time";
    String METAL = "metal";
    String LIVES = "lives";
    String TIMEITEM = "tItem";
    String METALITEM = "sItem";
    String LIVESITEM = "lItem";


    public FlashingMenuTest(MenuAPI plugin) {
        this.plugin = plugin;
        this.size = MenuSize.FIVEFOUR;
    }

    public void open(Player player, int lives) {

        // Create and decorate the menu
        Decoration deco = new Decoration(size, Material.BLACK_STAINED_GLASS_PANE, Scheme.TOPBOTTOM);
        GameMenu menu = MenuAPI.createGameMenu(plugin, BLUE + "Temper the Metal", size, deco, 40);

        menu.setVar(START, false);
        menu.setVar(TIME, 20);
        menu.setVar(TIMEITEM, new MenuItem(new ItemStack(Material.CLOCK, 15), GREEN + "Start"));
        menu.setVar(ITEMS, new ArrayList<Integer>());
        menu.setVar(METAL, lives);
        menu.setVar(LIVES, 2);
        menu.setVar(METALITEM, new MenuItem(new ItemStack(Material.LAVA_BUCKET, lives), RED + "Molten Metal Left", "Miss an Axe, lose 1 Molten", "Metal. Run out of Molten Metal", "and fail to Temper it"));
        menu.setVar(LIVESITEM, new MenuItem(new ItemStack(Material.ANVIL, 3), RED + "Anvil Health", "Hit an Anvil and lose 1", "Anvil Health. If it hits ", "zero you'll fail to temper", "the metal."));

        // How to Play
        menu.addItemToPage(0, 4, new MenuItem(Material.OAK_SIGN, GOLD + "Tempering Metal", "You need to strike the metal",
                "while it's still hot. Wait", "too long and you'll lose", "some metal. Run out of Molten", "Metal and you'll fail", "to temper it properly.", "Avoid striking the anvil", "directly or it may break.", "Also, avoid hitting any", "fully molten firey metal."));

        // Lives Display
        menu.addItemToPage(0, 48, menu.getVarMItem(LIVESITEM));

        // Start Button
        menu.addItemToPage(0, 49, menu.getVarMItem(TIMEITEM));

        // Score Display
        menu.addItemToPage(0, 50, menu.getVarMItem(METALITEM));

        // Brown Floor
        for (int i = 9; i < 45; i++) {
            menu.addItemToPage(0, i, new MenuItem(Material.RED_STAINED_GLASS_PANE, " "));
        }

        // Click Action
        ClickCallback callback = new ClickCallback() {
            public void callback(Menu menu, MenuPage page, ItemStack item, int slot, Player clicker, InventoryClickEvent event) {
                GameMenu gMenu = (GameMenu) menu;
                List<Integer> items = gMenu.getVarIntList(ITEMS);
                if (slot == 49) {
                    gMenu.setVar(START, true);
                    clicker.playSound(clicker.getLocation(), Sound.UI_BUTTON_CLICK, 0.1f, 2.0f);
                } else if (items.contains(slot)) {
                    // This line uses the head slot in the var list to get the item from the menupage.
                    Material type = page.getItem(slot).getItem().getType();
                    switch (type) {
                        case IRON_AXE:
                            clicker.playSound(clicker.getLocation(), Sound.BLOCK_ANVIL_HIT, 0.1f, 2.0f);
                            removeItem(gMenu, slot);
                            break;
                        case BLAZE_POWDER:
                            clicker.playSound(clicker.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 0.1f, 1.0f);
                            removeMetal(gMenu, slot, 5);
                            break;
                        case DAMAGED_ANVIL:
                            clicker.playSound(clicker.getLocation(), Sound.BLOCK_ANVIL_FALL, 0.1f, 0.5f);
                            removeLife(gMenu, slot);
                            break;
                        default:
                    }
                }
            }
        };

        // Animation Action
        AnimationCallback animation = new AnimationCallback() {
            public void callback(AnimatedMenu menu) {
                GameMenu gMenu = (GameMenu) menu;
                if (!gMenu.getVarBool(START)) return;
                List<Integer> items = gMenu.getVarIntList(ITEMS);

                Player viewer = menu.getViewer();

                // Remove old heads and remove lives for each missed zombie one.
                if (items.size() > 0) {
                    boolean takeDamage = false;
                    List<Integer> remaining = new ArrayList<Integer>(items);
                    MenuPage page = menu.getPage(0);
                    for (Integer slot : remaining) {
                        if (page.getItem(slot).getItem().getType().equals(Material.IRON_AXE)) {
                            removeMetal(gMenu, slot, 1);
                            takeDamage = true;
                        } else {
                            removeItem(gMenu, slot);
                        }
                    }
                    if (takeDamage) viewer.playSound(viewer.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 0.1f, 1.0f);
                    else viewer.playSound(viewer.getLocation(), Sound.UI_BUTTON_CLICK, 0.1f, 2.0f);
                }

                // Removes time
                removeTime(gMenu);

                // Add new heads
                int newHeads = getRand(8, 12);
                for (int i = 0; i <= newHeads; i++) {
                    int headtype = getRand(0, 10);
                    if (headtype > 8) {
                        addItem(gMenu, getRand(9, 44), Material.BLAZE_POWDER);
                    } else if (headtype < 3) {
                        addItem(gMenu, getRand(9, 44), Material.DAMAGED_ANVIL);
                    } else {
                        addItem(gMenu, getRand(9, 44), Material.IRON_AXE);
                    }
                }
            }

            public void callback(AnimatedListMenu menu) {

            }
        };

        // Set actions and open menu
        menu.setAnimation(animation);
        menu.setCallbackHandler(callback);
        menu.open(player);
    }

    private void removeMetal(GameMenu menu, Integer slot, int amount) {
        menu.setVar(METAL, menu.getVarInt(METAL) - amount);
        if (menu.getVarInt(METAL) <= 0) lose(menu);
        menu.updateItemAmount(menu.getVarMItem(METALITEM), menu.getVarInt(METAL));
        removeItem(menu, slot);
    }

    private void removeLife(GameMenu menu, Integer slot) {
        menu.setVar(LIVES, menu.getVarInt(LIVES) - 1);
        if (menu.getVarInt(LIVES) <= 0) lose(menu);
        menu.updateItemAmount(menu.getVarMItem(LIVESITEM), menu.getVarInt(LIVES));
        removeItem(menu, slot);
    }

    private void removeTime(GameMenu menu) {
        menu.setVar(TIME, menu.getVarInt(TIME) - 1);
        if (menu.getVarInt(TIME) <= 0) win(menu);
        menu.updateItemAmount(menu.getVarMItem(TIMEITEM), menu.getVarInt(TIME));
    }

    private void addItem(GameMenu menu, int slot, Material headtype) {
        MenuItem head = new MenuItem(headtype, " ");
        menu.removeItem(0, slot);
        menu.addItemToPage(0, slot, head);
        menu.getVarIntList(ITEMS).add(slot);
    }

    private void removeItem(GameMenu menu, Integer slot) {
        menu.removeItem(0, slot);
        menu.addItemToPage(0, slot, new MenuItem(Material.RED_STAINED_GLASS_PANE, " "));
        menu.getVarIntList(ITEMS).remove(slot);
    }

    private void win(GameMenu menu) {
        Player viewer = menu.getViewer();
        menu.close(viewer);

        viewer.playSound(viewer.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 0.1f, 1.0f);
        viewer.sendTitle(GOLD + "You Win", GRAY + "You've earned a diamond", 20, 20, 40);
        viewer.getInventory().addItem(new ItemStack(Material.DIAMOND));
    }

    private void lose(GameMenu menu) {
        Player viewer = menu.getViewer();
        menu.close(viewer);
        viewer.sendTitle(RED + "You Lose", GRAY + "Better luck next time!", 20, 20, 40);
    }

    private int getRand(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

}

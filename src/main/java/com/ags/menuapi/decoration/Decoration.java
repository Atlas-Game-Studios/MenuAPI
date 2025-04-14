package com.ags.menuapi.decoration;

import com.ags.atlaslib.util.PDC;
import com.ags.menuapi.Menu.MenuSize;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Decoration {

    ItemStack[] itemMap;
    MenuSize size;

    private Decoration(MenuSize size) {
        this.size = size;
        this.itemMap = new ItemStack[size.toNumber()];
    }

    /**
     * Decoration - Create a new Decoration with a pre-configured scheme.
     *
     * @param size   - The size of the menu. This must match the size of menu you created
     * @param mat    - the Material to use as the decoration item
     * @param scheme - the Scheme for the decorations.
     */
    public Decoration(MenuSize size, Material mat, Scheme scheme) {
        this(size);
        setScheme(mat, scheme);
    }

    /**
     * Decoration - Create a new Decoration with a custome scheme.
     *
     * @param size  - The size of the menu. This must match the size of menu you created
     * @param mat   - the Material to use as the decoration item
     * @param slots - The custom List of slots to put the decoration Material in.
     */
    public Decoration(MenuSize size, Material mat, List<Integer> slots) {
        this(size);
        setCustomScheme(mat, slots);
    }

    public ItemStack[] getItemMap() {
        return itemMap;
    }


    private void setScheme(Material mat, Scheme scheme) {
        for (int i = 0; i < itemMap.length; i++) {
            itemMap[i] = new ItemStack(Material.AIR);
        }
        for (int slot : scheme.getSlotsArray(size)) {
            makeDecorationItem(mat, slot);
        }
    }

    private void setCustomScheme(Material mat, List<Integer> slots) {
        for (int i = 0; i < itemMap.length; i++) {
            itemMap[i] = new ItemStack(Material.AIR);
        }
        for (int slot : slots) {
            makeDecorationItem(mat, slot);
        }
    }

    private void makeDecorationItem(Material mat, int slot) {
        ItemStack item = new ItemStack(mat);
        if (!mat.isAir()) {
            ItemMeta meta = item.getItemMeta();
            meta.displayName(Component.text(" "));
            meta.setHideTooltip(true);
            meta.setCustomModelData(1);
            item.setItemMeta(meta);
            PDC.set(item, "menu", true);
        }
        itemMap[slot] = item;
    }
}

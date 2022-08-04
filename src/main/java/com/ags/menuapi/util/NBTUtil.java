package com.ags.menuapi.util;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class NBTUtil {

    public static ItemStack setTag(ItemStack item, String key, Object value) {
        if (item == null) return item;
        NBTItem nbtItem = new NBTItem(item);
        setTag(nbtItem, key, value);
        return nbtItem.getItem();
    }

    public static ItemStack addTag(ItemStack item, String key, Object value) {
        if (item == null) return item;
        NBTItem nbtItem = new NBTItem(item);
        setTag(nbtItem, key, value);
        return nbtItem.getItem();
    }

    public static ItemStack setTags(ItemStack item, Map<String, Object> map) {
        if (item == null) return null;
        NBTItem nbtItem = new NBTItem(item);
        for (String key : map.keySet()) {
            setTag(nbtItem, key, map.get(key));
        }
        return nbtItem.getItem();
    }

    public static void setTag(NBTItem nbtItem, String key, Object value) {
        if (value instanceof Boolean) nbtItem.setBoolean(key, (Boolean) value);
        else if (value instanceof Byte) nbtItem.setByte(key, (Byte) value);
        else if (value instanceof byte[]) nbtItem.setByteArray(key, (byte[]) value);
        else if (value instanceof Double) nbtItem.setDouble(key, (Double) value);
        else if (value instanceof Float) nbtItem.setByte(key, (Byte) value);
        else if (value instanceof int[]) nbtItem.setIntArray(key, (int[]) value);
        else if (value instanceof Integer) nbtItem.setInteger(key, (Integer) value);
        else if (value instanceof ItemStack) nbtItem.setItemStack(key, (ItemStack) value);
        else if (value instanceof Long) nbtItem.setLong(key, (Long) value);
        else if (value instanceof Short) nbtItem.setShort(key, (Short) value);
        else if (value instanceof String) nbtItem.setString(key, (String) value);
        else nbtItem.setObject(key, value);
    }


    public static boolean hasKey(ItemStack item, String nbtkey) {
        if (item == null) return false;
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.hasNBTData() && nbtItem.hasKey(nbtkey);
    }

    public static boolean hasValue(ItemStack item, String key, Object value) {
        if (item == null) return false;
        NBTItem nbtItem = new NBTItem(item);
        if (nbtItem.hasNBTData() && nbtItem.hasKey(key)) {
            return nbtItem.getObject(key, value.getClass()).equals(value);
        }
        return false;
    }

    public static Boolean getBoolean(ItemStack item, String key) {
        if (item == null) return null;
        NBTItem nbtItem = new NBTItem(item);
        if (nbtItem.hasNBTData() && nbtItem.hasKey(key)) {
            return nbtItem.getBoolean(key);
        }
        return null;
    }

    public static Byte getByte(ItemStack item, String key) {
        if (item == null) return null;
        NBTItem nbtItem = new NBTItem(item);
        if (nbtItem.hasNBTData() && nbtItem.hasKey(key)) {
            return nbtItem.getByte(key);
        }
        return null;
    }

    public static byte[] getByteArray(ItemStack item, String key) {
        if (item == null) return null;
        NBTItem nbtItem = new NBTItem(item);
        if (nbtItem.hasNBTData() && nbtItem.hasKey(key)) {
            return nbtItem.getByteArray(key);
        }
        return null;
    }

    public static Double getDouble(ItemStack item, String key) {
        if (item == null) return null;
        NBTItem nbtItem = new NBTItem(item);
        if (nbtItem.hasNBTData() && nbtItem.hasKey(key)) {
            return nbtItem.getDouble(key);
        }
        return null;
    }

    public static Float getFloat(ItemStack item, String key) {
        if (item == null) return null;
        NBTItem nbtItem = new NBTItem(item);
        if (nbtItem.hasNBTData() && nbtItem.hasKey(key)) {
            return nbtItem.getFloat(key);
        }
        return null;
    }

    public static int[] getIntArray(ItemStack item, String key) {
        if (item == null) return null;
        NBTItem nbtItem = new NBTItem(item);
        if (nbtItem.hasNBTData() && nbtItem.hasKey(key)) {
            return nbtItem.getIntArray(key);
        }
        return null;
    }

    public static Integer getInteger(ItemStack item, String key) {
        if (item == null) return null;
        NBTItem nbtItem = new NBTItem(item);
        if (nbtItem.hasNBTData() && nbtItem.hasKey(key)) {
            return nbtItem.getInteger(key);
        }
        return null;
    }

    public static ItemStack getItemStack(ItemStack item, String key) {
        if (item == null) return null;
        NBTItem nbtItem = new NBTItem(item);
        if (nbtItem.hasNBTData() && nbtItem.hasKey(key)) {
            return nbtItem.getItemStack(key);
        }
        return null;
    }

    public static Long getLong(ItemStack item, String key) {
        if (item == null) return null;
        NBTItem nbtItem = new NBTItem(item);
        if (nbtItem.hasNBTData() && nbtItem.hasKey(key)) {
            return nbtItem.getLong(key);
        }
        return null;
    }

    public static Short getShort(ItemStack item, String key) {
        if (item == null) return null;
        NBTItem nbtItem = new NBTItem(item);
        if (nbtItem.hasNBTData() && nbtItem.hasKey(key)) {
            return nbtItem.getShort(key);
        }
        return null;
    }

    public static String getString(ItemStack item, String key) {
        if (item == null) return null;
        NBTItem nbtItem = new NBTItem(item);
        if (nbtItem.hasNBTData() && nbtItem.hasKey(key)) {
            return nbtItem.getString(key);
        }
        return null;
    }

    // ATLAS SPECIFIC

    public static ItemStack addTypeNBT(ItemStack item, String type) {
        if (item == null) return null;
        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setString("Type", type);
        return nbtItem.getItem();
    }

    public static boolean isType(ItemStack item) {
        if (item == null) return false;
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.hasKey("Type");
    }

    public static boolean isType(ItemStack item, String typeName) {
        if (item == null) return false;
        NBTItem nbtItem = new NBTItem(item);
        if (!nbtItem.hasNBTData() || !nbtItem.hasKey("Type")) return false;
        return nbtItem.getString("Type").equalsIgnoreCase(typeName);
    }

    public static String getTypeNBT(ItemStack item) {
        if (item == null) return null;
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.getString("Type");
    }

    public static ItemStack addMaterialNBT(ItemStack item, String name) {
        if (item == null) return null;
        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setString("Material", name);
        return nbtItem.getItem();
    }

    public static boolean isMaterial(ItemStack item) {
        if (item == null) return false;
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.hasKey("Material");
    }

    public static boolean isMaterial(ItemStack item, String materialName) {
        if (item == null) return false;
        NBTItem nbtItem = new NBTItem(item);
        if (!nbtItem.hasNBTData() || !nbtItem.hasKey("Material")) return false;
        return nbtItem.getString("Material").equalsIgnoreCase(materialName);
    }

    public static String getMaterialNBT(ItemStack item) {
        if (item == null) return null;
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.getString("Material");
    }

    public static ItemStack addRarityNBT(ItemStack item, String name) {
        if (item == null) return null;
        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setString("Rarity", name);
        return nbtItem.getItem();
    }

    public static boolean isRarity(ItemStack item) {
        if (item == null) return false;
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.hasKey("Rarity");
    }

    public static boolean isRarity(ItemStack item, String materialName) {
        if (item == null) return false;
        NBTItem nbtItem = new NBTItem(item);
        if (!nbtItem.hasNBTData() || !nbtItem.hasKey("Rarity")) return false;
        return nbtItem.getString("Rarity").equalsIgnoreCase(materialName);
    }

    public static String getRarityNBT(ItemStack item) {
        if (item == null) return null;
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.getString("Rarity");
    }

}

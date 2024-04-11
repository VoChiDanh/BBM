package net.danh.bbm.utils;


import com.google.common.util.concurrent.AtomicDouble;
import de.tr7zw.changeme.nbtapi.NBT;
import net.danh.bbm.resources.Chat;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class ItemEditor {
    private ItemStack is;
    private ItemMeta itemMeta;

    public ItemEditor(Material m) {
        this(m, 1);
    }

    public ItemEditor(ItemStack is) {
        this.is = is;
        itemMeta = is.getItemMeta();
    }

    public ItemEditor(Material m, int amount) {
        is = new ItemStack(m, amount);
        itemMeta = is.getItemMeta();
    }

    public ItemEditor setMaterial(Material material) {
        is.setType(material);
        return this;
    }

    public ItemEditor clone() {
        return new ItemEditor(is);
    }

    public String getName() {
        return itemMeta.getDisplayName();
    }

    public ItemEditor setName(String name) {
        itemMeta.setDisplayName(Chat.normalColorize(name));
        return this;
    }

    public ItemEditor addLoreLines(String... lines) {
        List<String> lore = new ArrayList<>();
        if (itemMeta.hasLore()) lore = new ArrayList<>(itemMeta.getLore());
        for (String line : lines) {
            lore.add(Chat.normalColorize(line));
        }
        itemMeta.setLore(lore);
        return this;
    }

    public ItemEditor addAllLore(ArrayList<String> lore2) {
        List<String> lore = new ArrayList<>();
        if (itemMeta.hasLore()) lore = new ArrayList<>(itemMeta.getLore());
        for (String line : lore2) {
            lore.add(Chat.normalColorize(line));
        }
        itemMeta.setLore(lore);
        return this;
    }

    public ItemEditor setNBTBoolean(String key, boolean state) {
        NBT.modify(is, nbt -> {
            nbt.setBoolean(key, state);
        });
        itemMeta = is.getItemMeta();
        return this;
    }

    public ItemEditor setNBTInt(String key, int state) {
        NBT.modify(is, nbt -> {
            nbt.setInteger(key, state);
        });
        itemMeta = is.getItemMeta();
        return this;
    }


    public ItemEditor setNBTDouble(String key, double state) {
        NBT.modify(is, nbt -> {
            nbt.setDouble(key, state);
        });
        itemMeta = is.getItemMeta();
        return this;
    }

    public ItemEditor setNBTString(String key, String information) {
        NBT.modify(is, nbt -> {
            nbt.setString(key, information);
        });
        itemMeta = is.getItemMeta();
        return this;
    }

    public boolean getNBTBoolean(String tag) {
        AtomicBoolean nbtData = new AtomicBoolean(false);
        NBT.get(is, nbt -> {
            nbtData.set(nbt.getBoolean(tag));
        });
        return nbtData.get();
    }

    public int getNBTInt(String tag) {
        AtomicInteger nbtData = new AtomicInteger();
        NBT.get(is, nbt -> {
            nbtData.set(nbt.getInteger(tag));
        });
        return nbtData.get();
    }

    public double getNBTDouble(String tag) {
        AtomicDouble nbtData = new AtomicDouble();
        NBT.get(is, nbt -> {
            nbtData.set(nbt.getDouble(tag));
        });
        return nbtData.get();
    }

    public String getNBTString(String tag) {
        AtomicReference<String> nbtData = new AtomicReference<>();
        NBT.get(is, nbt -> {
            nbtData.set(nbt.getString(tag));
        });
        return nbtData.get();
    }

    public ItemStack toItemStack() {
        is.setItemMeta(itemMeta);
        return is;
    }
}

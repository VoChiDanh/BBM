package net.danh.bbm.gui;

import io.lumine.mythic.lib.api.item.NBTItem;
import me.clip.placeholderapi.PlaceholderAPI;
import net.Indyuce.mmoitems.MMOItems;
import net.danh.bbm.resources.Chat;
import net.danh.bbm.resources.Files;
import net.danh.bbm.resources.Number;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InvClick implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onClick(@NotNull InventoryClickEvent e) {
        if (e.getView().getTitle().equalsIgnoreCase(Chat.normalColorize(Files.getItemUpgrade().getString("gui_upgrade.title")))) {
            e.setCancelled(true);
            if (e.getWhoClicked() instanceof Player p) {
                FileConfiguration config = Files.getItemUpgrade();
                String gui_path = "gui_upgrade.items";
                for (String item_id : Objects.requireNonNull(config.getConfigurationSection(gui_path)).getKeys(false)) {
                    String item_path = gui_path + "." + item_id;
                    List<Integer> slots = new ArrayList<>();
                    if (config.contains(item_path + ".slots"))
                        slots.addAll(config.getIntegerList(item_path + ".slots"));
                    if (config.contains(item_path + ".slot"))
                        slots.add(config.getInt(item_path + ".slot"));
                    if (slots.contains(e.getSlot())) {
                        if (config.contains(item_path + ".action")) {
                            String action = config.getString(item_path + ".action");
                            if (action != null) {
                                if (action.equalsIgnoreCase("confirm_upgrade")) {
                                    NBTItem nbtItem = NBTItem.get(p.getInventory().getItemInMainHand());
                                    if (nbtItem.hasType()) {
                                        String type = nbtItem.getType();
                                        String item_id_check = nbtItem.getString("MMOITEMS_ITEM_ID");
                                        String final_id;
                                        int level = 0;
                                        int index = item_id_check.lastIndexOf('_');
                                        if (index != -1 && index < item_id_check.length() - 1) {
                                            final_id = item_id_check.substring(0, index);
                                            level = Number.getInteger(item_id_check.substring(index + 1));
                                        } else {
                                            final_id = item_id_check;
                                        }
                                        String result_id = final_id + "_" + (level + 1);
                                        ItemStack nextItem = MMOItems.plugin.getItem(type, result_id);
                                        if (nextItem != null) {
                                            List<String> requirementsItem = PlaceholderAPI.setPlaceholders(p,
                                                    config.getStringList("item_upgrade." + type + ";" + final_id + ".item_requirements.ingredients_" + level));
                                            int meetRequirements = ItemUpgrade.getMeetItemsRequirement(p, requirementsItem);
                                            if (meetRequirements == requirementsItem.size()) {
                                                p.closeInventory();
                                                p.getInventory().setItemInMainHand(nextItem);
                                                Chat.sendMessage(p,
                                                        Files.getMessage().getString("user.upgrade_item.upgrade_success"));
                                            } else {
                                                Chat.sendMessage(p,
                                                        Files.getMessage().getString("user.upgrade_item.not_enough_item"));
                                            }
                                        } else {
                                            Chat.sendMessage(p,
                                                    Files.getMessage().getString("user.upgrade_item.reach_max_upgrade"));
                                        }
                                    }
                                } else if (action.equalsIgnoreCase("force_upgrade")) {
                                    NBTItem nbtItem = NBTItem.get(p.getInventory().getItemInMainHand());
                                    if (nbtItem.hasType()) {
                                        String type = nbtItem.getType();
                                        String item_id_check = nbtItem.getString("MMOITEMS_ITEM_ID");
                                        String final_id;
                                        int level = 0;
                                        int index = item_id_check.lastIndexOf('_');
                                        if (index != -1 && index < item_id_check.length() - 1) {
                                            final_id = item_id_check.substring(0, index);
                                            level = Number.getInteger(item_id_check.substring(index + 1));
                                        } else {
                                            final_id = item_id_check;
                                        }
                                        String result_id = final_id + "_" + (level + 1);
                                        ItemStack nextItem = MMOItems.plugin.getItem(type, result_id);
                                        if (nextItem != null) {
                                            List<String> requirementsItem = PlaceholderAPI.setPlaceholders(p,
                                                    config.getStringList(item_path + ".force_upgrade_item"));
                                            int meetRequirements = ItemUpgrade.getMeetItemsRequirement(p, requirementsItem);
                                            if (meetRequirements == requirementsItem.size()) {
                                                p.closeInventory();
                                                p.getInventory().setItemInMainHand(nextItem);
                                                Chat.sendMessage(p,
                                                        Files.getMessage().getString("user.upgrade_item.upgrade_success"));
                                            } else {
                                                Chat.sendMessage(p,
                                                        Files.getMessage().getString("user.upgrade_item.not_enough_item"));
                                            }
                                        } else {
                                            Chat.sendMessage(p,
                                                    Files.getMessage().getString("user.upgrade_item.reach_max_upgrade"));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

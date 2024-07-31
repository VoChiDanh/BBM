package net.danh.bbm.listeners;

import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import io.lumine.mythic.bukkit.utils.serialize.Position;
import io.lumine.mythic.core.mobs.ActiveMob;
import io.lumine.mythic.lib.api.item.NBTItem;
import net.danh.bbm.BBM;
import net.danh.bbm.resources.Chat;
import net.danh.bbm.resources.Files;
import net.danh.mineregion.API.Events.MiningEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Mining implements Listener {

    public static HashMap<String, ActiveMob> mobData = new HashMap<>();

    @EventHandler
    public void onMiningRegion(@NotNull MiningEvent e) {
        Player p = e.getPlayer();
        Block b = e.getBlock();
        Location block_location = e.getBlockLocation();
        ItemStack itemStack = p.getInventory().getItemInMainHand();
        if (itemStack.getType() != Material.AIR) {
            Chat.debug("Hand has item");
            NBTItem nbtItem = NBTItem.get(itemStack);
            int system_spawn = ThreadLocalRandom.current().nextInt(0, 100);
            if (system_spawn > 0) {
                Chat.debug("System spawn: " + system_spawn);
                for (String block : Objects.requireNonNull(Files.getConfig().getConfigurationSection("blocks")).getKeys(false)) {
                    Chat.debug(b.getType().toString());
                    Chat.debug(block);
                    if (b.getType().toString().equalsIgnoreCase(block)) {
                        Chat.debug("Has block");
                        String mob_spawn = Files.getConfig().getString("blocks." + block + ".mob_spawn");
                        int spawn_chance = Files.getConfig().getInt("blocks." + block + ".chance");
                        if (nbtItem.hasTag("MMOITEMS_BBM_REDUCE_MOB_SPAWN")) {
                            Chat.debug("Hand stats");
                            int reduce_chance = nbtItem.getInteger("MMOITEMS_BBM_REDUCE_MOB_SPAWN");
                            if (spawn_chance > (system_spawn + reduce_chance)) {
                                Chat.debug("Spawn chance: " + spawn_chance);
                                Chat.debug("Real system chance: " + (system_spawn + reduce_chance));
                                MythicMob mob = MythicBukkit.inst().getAPIHelper().getMythicMob(mob_spawn);
                                if (mob != null) {
                                    Chat.debug("Mob is not null");
                                    Chat.sendMessage(p, Files.getMessage().getStringList("user.spawn_mob")
                                            .stream().map(s -> s.replace("<name>", mob.getDisplayName().toString())).collect(Collectors.toList()));
                                    ActiveMob activeMob = mob.spawn(new AbstractLocation(Position.of(block_location.add(0, 3, 0))), 1);
                                    activeMob.setTarget(BukkitAdapter.adapt(p));
                                    mobData.put(p.getName() + "_" + activeMob.getUniqueId() + "_" + UUID.randomUUID(), activeMob);
                                    Bukkit.getOnlinePlayers().forEach(player -> {
                                        if (player != p) {
                                            player.hideEntity(BBM.getBBMCore(), activeMob.getEntity().getBukkitEntity());
                                            activeMob.setOwner(BukkitAdapter.adapt(player).getUniqueId());
                                        }
                                    });
                                }
                            }
                        } else {
                            if (spawn_chance > system_spawn) {
                                Chat.debug("Spawn chance: " + spawn_chance);
                                Chat.debug("Real system chance: " + system_spawn);
                                MythicMob mob = MythicBukkit.inst().getAPIHelper().getMythicMob(mob_spawn);
                                if (mob != null) {
                                    Chat.debug("Mob is not null");
                                    Chat.sendMessage(p, Files.getMessage().getStringList("user.spawn_mob")
                                            .stream().map(s -> s.replace("<name>", mob.getDisplayName().toString())).collect(Collectors.toList()));
                                    ActiveMob activeMob = mob.spawn(new AbstractLocation(Position.of(block_location.add(0, 3, 0))), 1);
                                    activeMob.setTarget(BukkitAdapter.adapt(p));
                                    mobData.put(p.getName() + "_" + activeMob.getUniqueId() + "_" + UUID.randomUUID(), activeMob);
                                    Bukkit.getOnlinePlayers().forEach(player -> {
                                        if (player != p) {
                                            player.hideEntity(BBM.getBBMCore(), activeMob.getEntity().getBukkitEntity());
                                            activeMob.setOwner(BukkitAdapter.adapt(player).getUniqueId());
                                        }
                                    });
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onMythicDead(MythicMobDeathEvent e) {
        if (e.getKiller() instanceof Player p) {
            if (!mobData.isEmpty())
                for (String data : mobData.keySet()) {
                    if (data.startsWith(p.getName())) {
                        if (mobData.get(data) == null || mobData.get(data).isDead()
                                || mobData.get(data).getUniqueId().equals(e.getEntity().getUniqueId())) {
                            mobData.remove(data, mobData.get(data));
                            break;
                        }
                    }
                }
        }
    }
}

package net.danh.bbm.listeners;

import net.danh.bbm.BBM;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

public class VoidDeath implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(@NotNull EntityDamageEvent e) {
        if (e.getEntity() instanceof Player p) {
            if (e.getCause() == EntityDamageEvent.DamageCause.VOID) {
                e.setCancelled(true);
                p.setHealth(0);
                Bukkit.getScheduler().scheduleSyncDelayedTask(BBM.getBBMCore(), () -> p.spigot().respawn(), 0);
            }
        }
    }
}

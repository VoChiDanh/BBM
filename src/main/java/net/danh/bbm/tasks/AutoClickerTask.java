package net.danh.bbm.tasks;

import io.lumine.mythic.lib.api.item.NBTItem;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoClickerTask extends BukkitRunnable {

    private final Player player;
    private final double cooldown;
    private final int range;

    public AutoClickerTask(Player player, double cooldown, int range) {
        this.player = player;
        this.cooldown = cooldown;
        this.range = range;
    }

    @Override
    public void run() {
        if (!player.isOnline() || player.isDead()) {
            AutoClicker.stopAutoClicker(player);
            return;
        }

        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        ItemStack itemInOffHand = player.getInventory().getItemInOffHand();
        NBTItem nbtItem = NBTItem.get(itemInHand);
        NBTItem nbtOffItem = NBTItem.get(itemInOffHand);
        double damage = (nbtItem != null && nbtOffItem.hasType() && nbtItem.hasTag("MMOITEMS_ATTACK_DAMAGE")) ? nbtItem.getDouble("MMOITEMS_ATTACK_DAMAGE")
                : (nbtOffItem != null && nbtOffItem.hasType() && nbtOffItem.hasTag("MMOITEMS_ATTACK_DAMAGE")) ? nbtOffItem.getDouble("MMOITEMS_ATTACK_DAMAGE") : 0;
        if (damage > 0) {
            for (Entity entity : player.getNearbyEntities(range, range, range)) {
                if (entity instanceof LivingEntity target && !(entity instanceof Player)) {
                    target.damage(damage, player);
                }
            }
        }
    }
}

package net.danh.bbm.tasks;

import net.danh.bbm.BBM;
import net.danh.bbm.resources.Chat;
import net.danh.bbm.resources.Files;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitTask;

public class AutoClicker {

    public static void startAutoClicker(Player player) {
        BukkitTask task = new AutoClickerTask(player, 1, 2).runTaskTimer(BBM.getBBMCore(), 0L, 20L);
        player.setMetadata("autoclicker_task", new FixedMetadataValue(BBM.getBBMCore(), task));
        String message = Files.getMessage().getString("user.auto_click_on");
        if (message != null && !message.isEmpty()) {
            player.sendMessage(Chat.colorizewp(message));
        }
    }

    public static void stopAutoClicker(Player player) {
        if (player.hasMetadata("autoclicker_task")) {
            BukkitTask task = (BukkitTask) player.getMetadata("autoclicker_task").get(0).value();
            task.cancel();
            player.removeMetadata("autoclicker_task", BBM.getBBMCore());
            String message = Files.getMessage().getString("user.auto_click_off");
            if (message != null && !message.isEmpty()) {
                player.sendMessage(Chat.colorizewp(message));
            }
        }
    }
}

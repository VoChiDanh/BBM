package net.danh.bbm.listeners;

import io.lumine.mythic.bukkit.BukkitAdapter;
import net.danh.bbm.BBM;
import net.danh.bbm.resources.Files;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class Join implements Listener {

    @EventHandler
    public void onJoin(@NotNull PlayerJoinEvent e) {
        String world_name = e.getPlayer().getWorld().getName();
        String game_mode = Files.getConfig().getString("gamemodes." + world_name, "survival");
        if (game_mode.equalsIgnoreCase("survival")) e.getPlayer().setGameMode(GameMode.SURVIVAL);
        else if (game_mode.equalsIgnoreCase("creative")) e.getPlayer().setGameMode(GameMode.CREATIVE);
        else if (game_mode.equalsIgnoreCase("adventure")) e.getPlayer().setGameMode(GameMode.ADVENTURE);
        else if (game_mode.equalsIgnoreCase("spectator")) e.getPlayer().setGameMode(GameMode.SPECTATOR);

        for (String data : Mining.mobData.keySet()) {
            if (!data.startsWith(e.getPlayer().getName())) {
                if (Mining.mobData.get(data) != null || !Mining.mobData.get(data).isDead()) {
                    e.getPlayer().hideEntity(BBM.getBBMCore(), Mining.mobData.get(data).getEntity().getBukkitEntity());
                    Mining.mobData.get(data).setOwner(BukkitAdapter.adapt(e.getPlayer()).getUniqueId());
                }
            }
        }
    }
}

package net.danh.bbm.listeners;

import net.danh.bbm.resources.Files;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.jetbrains.annotations.NotNull;

public class WorldSwitch implements Listener {
    @EventHandler
    public void onWorldSwitch(@NotNull PlayerChangedWorldEvent e) {
        String world_name = e.getPlayer().getWorld().getName();
        String game_mode = Files.getConfig().getString("gamemodes." + world_name, "survival");
        if (game_mode.equalsIgnoreCase("survival"))
            e.getPlayer().setGameMode(GameMode.SURVIVAL);
        else if (game_mode.equalsIgnoreCase("creative"))
            e.getPlayer().setGameMode(GameMode.CREATIVE);
        else if (game_mode.equalsIgnoreCase("adventure"))
            e.getPlayer().setGameMode(GameMode.ADVENTURE);
        else if (game_mode.equalsIgnoreCase("spectator"))
            e.getPlayer().setGameMode(GameMode.SPECTATOR);
    }
}

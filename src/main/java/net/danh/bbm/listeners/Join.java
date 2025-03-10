package net.danh.bbm.listeners;

import net.danh.bbm.playerdata.PlayerData;
import net.danh.bbm.resources.Files;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class Join implements Listener {

    @EventHandler
    public void onJoin(@NotNull PlayerJoinEvent e) {
        String world_name = e.getPlayer().getWorld().getName();
        new PlayerData(e.getPlayer()).loadData();
        String game_mode = Files.getConfig().getString("gamemodes." + world_name, "adventure");
        if (game_mode.equalsIgnoreCase("survival")) e.getPlayer().setGameMode(GameMode.SURVIVAL);
        else if (game_mode.equalsIgnoreCase("creative")) e.getPlayer().setGameMode(GameMode.CREATIVE);
        else if (game_mode.equalsIgnoreCase("adventure")) e.getPlayer().setGameMode(GameMode.ADVENTURE);
        else if (game_mode.equalsIgnoreCase("spectator")) e.getPlayer().setGameMode(GameMode.SPECTATOR);
    }

    @EventHandler
    public void onQuit(@NotNull PlayerQuitEvent e) {
        new PlayerData(e.getPlayer()).saveData();
    }
}

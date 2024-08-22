package net.danh.bbm;

import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.stat.type.ItemStat;
import net.danh.bbm.cmd.BBM_CMD;
import net.danh.bbm.gui.InvClick;
import net.danh.bbm.listeners.Arrow;
import net.danh.bbm.listeners.Join;
import net.danh.bbm.listeners.Mining;
import net.danh.bbm.listeners.WorldSwitch;
import net.danh.bbm.mythicdrop.MythicReg;
import net.danh.bbm.playerdata.PlayerData;
import net.danh.bbm.playerdata.exp.ExpBase;
import net.danh.bbm.playerdata.player.PlayerLevel;
import net.danh.bbm.resources.Files;
import net.danh.bbm.stats.ReduceMobSpawn;
import net.danh.bbm.utils.PAPI;
import net.xconfig.bukkit.model.SimpleConfigurationManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Arrays;

public final class BBM extends JavaPlugin {

    private static BBM bbm;

    public static BBM getBBMCore() {
        return bbm;
    }

    @Override
    public void onLoad() {
        bbm = this;
        SimpleConfigurationManager.register(bbm);
        registerStats(new ReduceMobSpawn());
    }

    @Override
    public void onEnable() {
        try {
            ExpBase.load();
        } catch (IOException e) {
            getLogger().warning("Has issues with ExpBase (exp.txt)");
            throw new RuntimeException(e);
        }
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PAPI().register();
        }
        Files.loadFiles();
        new BBM_CMD();
        registerEvents(new Mining(), new WorldSwitch(), new Join(), new MythicReg(), new Arrow(), new InvClick());
        Files.updateConfig();
        Files.updateMessage();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> Bukkit.getOnlinePlayers().forEach(PlayerLevel::syncXPBar), 1L, 1L);
    }

    @Override
    public void onDisable() {
        for (Player p : Bukkit.getOnlinePlayers())
            new PlayerData(p).saveData();
        Files.saveFiles();
    }

    private void registerEvents(Listener... listeners) {
        Arrays.asList(listeners).forEach(listener -> {
            Bukkit.getPluginManager().registerEvents(listener, bbm);
            getLogger().info("Registered Listener " + listener);
        });
    }

    private void registerStats(ItemStat<?, ?>... stats) {
        Arrays.asList(stats).forEach(stat -> {
            MMOItems.plugin.getStats().register(stat);
            getLogger().info("Registered Stat " + stat.getId() + " / " + stat.getName());
        });
    }
}

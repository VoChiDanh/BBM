package net.danh.bbm;

import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.stat.type.ItemStat;
import net.danh.bbm.cmd.BBM_CMD;
import net.danh.bbm.listeners.Join;
import net.danh.bbm.listeners.Mining;
import net.danh.bbm.listeners.WorldSwitch;
import net.danh.bbm.resources.Files;
import net.danh.bbm.stats.ReduceMobSpawn;
import net.xconfig.bukkit.model.SimpleConfigurationManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

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
        new BBM_CMD();
        Files.loadFiles();
        registerEvents(new Mining(), new WorldSwitch(), new Join());
    }

    @Override
    public void onDisable() {
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

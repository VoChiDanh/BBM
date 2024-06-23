package net.danh.bbm.playerdata;

import net.danh.bbm.BBM;
import net.danh.bbm.mythicdrop.MythicXP;
import net.danh.bbm.playerdata.player.PlayerLevel;
import net.xconfig.bukkit.model.SimpleConfigurationManager;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class PlayerData {

    private final Player p;
    private File playerdataFile;
    private FileConfiguration playerdata;

    public PlayerData(Player p) {
        this.p = p;
    }

    public void loadData() {
        create();
        PlayerLevel.setPlayerLevel(p, Math.max(1, get().getInt("playerdata.main.level")));
        PlayerLevel.setPlayerEXP(p, Math.max(0, get().getInt("playerdata.main.exp")));
        MythicXP.booster.put(p, Math.max(1.0, get().getDouble("booster.permanently")));
        MythicXP.booster_temporary_times.put(p, Math.max(0, get().getInt("booster.temporary.times")));
        MythicXP.booster_temporary_multi.put(p, Math.max(1.0, get().getDouble("booster.temporary.multi")));
    }

    public void saveData() {
        create();
        get().set("name", p.getName());
        get().set("playerdata.main.level", Math.max(1, PlayerLevel.getPlayerLevel(p)));
        get().set("playerdata.main.exp", Math.max(0, PlayerLevel.getPlayerEXP(p)));
        get().set("booster.permanently", MythicXP.booster.get(p));
        get().set("booster.temporary.times", MythicXP.booster_temporary_times.get(p));
        get().set("booster.temporary.multi", MythicXP.booster_temporary_multi.get(p));
        save();
        reload();
    }

    private String getFileName() {
        return "playerData/" + p.getName() + "_" + p.getUniqueId() + ".yml";
    }

    public Player getPlayer() {
        return p;
    }

    public void create() {
        playerdataFile = new File(BBM.getBBMCore().getDataFolder(), getFileName());
        if (!playerdataFile.exists()) SimpleConfigurationManager.get().build("", true, getFileName());
        playerdata = new YamlConfiguration();

        try {
            playerdata.load(playerdataFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration get() {
        return playerdata;
    }

    public void reload() {
        playerdata = YamlConfiguration.loadConfiguration(playerdataFile);
    }

    public void save() {
        try {
            playerdata.save(playerdataFile);
        } catch (IOException ignored) {
        }
    }
}

package net.danh.bbm.resources;

import com.tchristofferson.configupdater.ConfigUpdater;
import net.danh.bbm.BBM;
import net.xconfig.bukkit.model.SimpleConfigurationManager;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class Files {

    public static void loadFiles() {
        SimpleConfigurationManager.get().build("", false, "config.yml", "message.yml", "playerData/example_data.yml");
    }

    public static void saveFiles() {
        SimpleConfigurationManager.get().save("config.yml", "message.yml", "playerData/example_data.yml");
    }

    public static void reloadFiles() {
        SimpleConfigurationManager.get().reload("config.yml", "message.yml", "playerData/example_data.yml");
    }

    public static FileConfiguration getConfig() {
        return SimpleConfigurationManager.get().get("config.yml");
    }

    public static FileConfiguration getMessage() {
        return SimpleConfigurationManager.get().get("message.yml");
    }


    public static void updateConfig() {
        SimpleConfigurationManager.get().save("config.yml");
        File configFile = new File(BBM.getBBMCore().getDataFolder(), "config.yml");
        try {
            ConfigUpdater.update(BBM.getBBMCore(), "config.yml", configFile, "blocks", "gamemodes", "booster", "times");
            BBM.getBBMCore().getLogger().log(Level.WARNING, "Your config have been updated successful");
        } catch (IOException e) {
            BBM.getBBMCore().getLogger().log(Level.WARNING, "Can not update config by it self, please backup and rename your config then restart to get newest config!!");
            e.printStackTrace();
        }
        SimpleConfigurationManager.get().reload("config.yml");
    }

    public static void updateMessage() {
        SimpleConfigurationManager.get().save("message.yml");
        File messageFile = new File(BBM.getBBMCore().getDataFolder(), "message.yml");
        try {
            ConfigUpdater.update(BBM.getBBMCore(), "message.yml", messageFile);
            BBM.getBBMCore().getLogger().log(Level.WARNING, "Your message have been updated successful");
        } catch (IOException e) {
            BBM.getBBMCore().getLogger().log(Level.WARNING, "Can not update message by it self, please backup and rename your message then restart to get newest message!!");
            e.printStackTrace();
        }
        SimpleConfigurationManager.get().reload("message.yml");
    }
}

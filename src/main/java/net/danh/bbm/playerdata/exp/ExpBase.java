package net.danh.bbm.playerdata.exp;

import net.danh.bbm.BBM;
import org.apache.commons.lang3.Validate;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ExpBase {

    private static final List<Integer> experience = new ArrayList<>();

    public static void load() throws IOException {
        if (!experience.isEmpty())
            experience.clear();
        loadTextFile();
        File file = new File(BBM.getBBMCore().getDataFolder(), "exp.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String readLine;
        while ((readLine = reader.readLine()) != null)
            experience.add(Integer.valueOf(readLine));
        reader.close();

        Validate.isTrue(!experience.isEmpty(), "There must be at least one exp value in your exp curve");
    }

    public static void loadTextFile() {
        File dataFolder = BBM.getBBMCore().getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        File file = new File(dataFolder, "exp.txt");
        if (!file.exists()) {
            BBM.getBBMCore().getLogger().info("File not found: " + "exp.txt" + ". Creating default file.");
            copyFileFromJar("exp.txt", file);
        }
    }

    private static void copyFileFromJar(String resourcePath, File destination) {
        try (InputStream in = BBM.getBBMCore().getResource(resourcePath)) {
            if (in == null) {
                BBM.getBBMCore().getLogger().severe("Default file " + resourcePath + " not found in JAR.");
            }
            Files.copy(in, destination.toPath());
            BBM.getBBMCore().getLogger().info("Default file " + resourcePath + " copied to " + destination.getPath());
        } catch (IOException e) {
            BBM.getBBMCore().getLogger().severe("Error copying default file: " + e.getMessage());
        }
    }

    public static int getExperience(int level) {
        Validate.isTrue(level > 0, "Level must be stricly positive");
        return experience.get(Math.min(level, experience.size()) - 1);
    }

    public static int getMaxLevel() {
        return experience.size() - 1;
    }
}

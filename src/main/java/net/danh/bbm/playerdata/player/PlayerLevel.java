package net.danh.bbm.playerdata.player;

import net.danh.bbm.playerdata.exp.ExpBase;
import net.danh.bbm.resources.Chat;
import net.danh.bbm.resources.Files;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang3.Validate;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlayerLevel {

    public static HashMap<Player, Integer> playerLevel = new HashMap<>();
    public static HashMap<Player, Integer> playerExp = new HashMap<>();


    public static int getPlayerLevel(Player player) {
        return playerLevel.getOrDefault(player, 0);
    }

    public static void setPlayerLevel(Player player, Integer level) {
        Validate.isTrue(level >= 0, "Level must be higher or equal 0!");
        if (!playerLevel.containsKey(player))
            playerLevel.put(player, level);
        else
            playerLevel.replace(player, level);
    }

    public static void addPlayerLevel(Player player, Integer level) {
        Validate.isTrue(level > 0, "Level must be higher than 0!");
        int newLevel = getPlayerLevel(player) + level;
        if (!playerLevel.containsKey(player))
            playerLevel.put(player, Math.min(newLevel, ExpBase.getMaxLevel()));
        else
            playerLevel.replace(player, Math.min(newLevel, ExpBase.getMaxLevel()));
    }

    public static void removePlayerLevel(Player player, Integer level) {
        Validate.isTrue(level > 0, "Level must be higher than 0!");
        int newLevel = getPlayerLevel(player) - level;
        if (!playerLevel.containsKey(player))
            playerLevel.put(player, Math.max(newLevel, 0));
        else
            playerLevel.replace(player, Math.max(newLevel, 0));
    }

    public static int getPlayerEXP(Player player) {
        return playerExp.getOrDefault(player, 0);
    }

    public static void setPlayerEXP(Player player, Integer exp) {
        if (!playerExp.containsKey(player))
            playerExp.put(player, exp);
        else
            playerExp.replace(player, exp);
    }

    public static void addPlayerEXP(Player player, Integer exp) {
        int newEXP = getPlayerEXP(player) + exp;
        int neededEXP = ExpBase.getExperience(getPlayerLevel(player));
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Chat.normalColorize(Files.getMessage().getString("user.exp_gain")
                .replace("<exp>", String.valueOf(exp))
                .replace("<new_exp>", String.valueOf(newEXP))
                .replace("<needed_exp>", String.valueOf(neededEXP)))));
        int conEXP = newEXP - neededEXP;
        if (neededEXP <= newEXP) {
            setPlayerEXP(player, 0);
            addPlayerLevel(player, 1);
            addPlayerEXP(player, Math.max(0, conEXP));
        } else {
            if (!playerExp.containsKey(player))
                playerExp.put(player, newEXP);
            else
                playerExp.replace(player, newEXP);
        }
    }

    public static void removePlayerEXP(Player player, Integer exp) {
        int newEXP = getPlayerEXP(player) - exp;
        if (0 > newEXP) {
            int conExp = exp - getPlayerEXP(player);
            setPlayerEXP(player, 0);
            removePlayerLevel(player, 1);
            if (conExp >= 0) {
                if (ExpBase.getExperience(getPlayerLevel(player)) - conExp >= 0)
                    setPlayerEXP(player, ExpBase.getExperience(getPlayerLevel(player)) - conExp);
                else {
                    setPlayerEXP(player, ExpBase.getExperience(getPlayerLevel(player)));
                    removePlayerEXP(player, ExpBase.getExperience(getPlayerLevel(player)) - conExp);
                }
            } else {
                setPlayerEXP(player, ExpBase.getExperience(getPlayerLevel(player)));
                removePlayerEXP(player, getPlayerEXP(player));
            }
        } else {
            if (!playerExp.containsKey(player))
                playerExp.put(player, newEXP);
            else
                playerExp.replace(player, newEXP);
        }
    }

    public static void syncXPBar(Player p) {
        float xp = getPlayerEXP(p);
        float level = getPlayerLevel(p);
        float req = ExpBase.getExperience(getPlayerLevel(p));
        if (req == 0) req = ExpBase.getExperience(getPlayerLevel(p));
        if (xp / req <= 1 && level >= 1) {
            p.setExp(Float.parseFloat(String.valueOf(xp / req)));
            p.setLevel((int) level);
        }
    }
}

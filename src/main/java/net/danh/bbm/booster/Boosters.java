package net.danh.bbm.booster;

import net.danh.bbm.calculator.Calculator;
import net.danh.bbm.mythicdrop.MythicXP;
import net.danh.bbm.resources.Files;
import org.bukkit.entity.Player;

import java.util.Objects;

public class Boosters {

    public static int getExp(Player p, int xp) {
        if (MythicXP.booster.containsKey(p)) {
            if (MythicXP.booster.get(p) > 1d) {
                if (MythicXP.booster_temporary_multi.get(p) > 1d) {
                    String booster_all_string = Objects.requireNonNull(Files.getConfig().getString("booster.mode.all"))
                            .replace("<xp>", String.valueOf(xp))
                            .replace("<p_multi>", String.valueOf(MythicXP.booster.get(p)))
                            .replace("<t_multi>", String.valueOf(MythicXP.booster_temporary_multi.get(p)))
                            .replace(",", ".");
                    String booster_all_calculator = Calculator.calculator(booster_all_string, 0);
                    return Math.abs((int) Double.parseDouble(booster_all_calculator));
                } else {
                    String booster_permanently_string = Objects.requireNonNull(Files.getConfig().getString("booster.mode.permanently"))
                            .replace("<xp>", String.valueOf(xp))
                            .replace("<p_multi>", String.valueOf(MythicXP.booster.get(p)))
                            .replace(",", ".");
                    String booster_permanently_calculator = Calculator.calculator(booster_permanently_string, 0);
                    return Math.abs((int) Double.parseDouble(booster_permanently_calculator));
                }
            } else {
                return xp;
            }
        } else {
            return xp;
        }
    }
}

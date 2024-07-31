package net.danh.bbm.utils;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.danh.bbm.BBM;
import net.danh.bbm.playerdata.exp.ExpBase;
import net.danh.bbm.playerdata.player.PlayerLevel;
import net.danh.bbm.resources.Number;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PAPI extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "bbm";
    }

    @Override
    public @NotNull String getAuthor() {
        return BBM.getBBMCore().getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return BBM.getBBMCore().getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player p, @NotNull String args) {
        if (p == null) return null;
        if (args.equalsIgnoreCase("level")) {
            return Number.intToSuffixedNumber(PlayerLevel.getPlayerLevel(p));
        }
        if (args.equalsIgnoreCase("exp")) {
            return Number.intToSuffixedNumber(PlayerLevel.getPlayerEXP(p));
        }
        if (args.equalsIgnoreCase("need_exp")) {
            return Number.intToSuffixedNumber(ExpBase.getExperience(PlayerLevel.getPlayerLevel(p)));
        }
        return null;
    }
}

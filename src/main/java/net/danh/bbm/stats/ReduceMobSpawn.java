package net.danh.bbm.stats;

import io.lumine.mythic.lib.api.item.NBTItem;
import net.Indyuce.mmoitems.api.player.RPGPlayer;
import net.Indyuce.mmoitems.stat.type.DoubleStat;
import net.Indyuce.mmoitems.stat.type.ItemRestriction;
import org.bukkit.Material;

public class ReduceMobSpawn extends DoubleStat implements ItemRestriction {
    public ReduceMobSpawn() {
        super("BBM_REDUCE_MOB_SPAWN", Material.WOODEN_PICKAXE, "Reduce Mob Spawning", new String[]{"Add chance to reduce mob spawning while mining"}, new String[]{"!block", "all"});
    }

    @Override
    public boolean canUse(RPGPlayer rpgPlayer, NBTItem nbtItem, boolean b) {
        return true;
    }
}

package net.danh.bbm.mythicdrop;

import io.lumine.mythic.bukkit.events.MythicDropLoadEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MythicReg implements Listener {

    @EventHandler
    public void onMythicDropLoad(MythicDropLoadEvent event) {
        if (event.getDropName().equalsIgnoreCase("bbm")) {
            event.register(new MythicXP(event.getConfig()));
        }
    }
}

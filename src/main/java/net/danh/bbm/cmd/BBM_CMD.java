package net.danh.bbm.cmd;

import net.danh.bbm.resources.Chat;
import net.danh.bbm.resources.Files;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BBM_CMD extends CMDBase {
    public BBM_CMD() {
        super("bbm");
    }

    @Override
    public void execute(@NotNull CommandSender c, String[] args) {
        if (c.hasPermission("bbm.admin")) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    Files.reloadFiles();
                    Chat.sendMessage(c, Files.getMessage().getString("admin.reload_files"));
                }
            }
        }
    }

    @Override
    public List<String> TabComplete(CommandSender sender, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();
        if (args.length == 1) {
            if (sender.hasPermission("bbm.admin")) {
                commands.add("reload");
            }
            StringUtil.copyPartialMatches(args[0], commands, completions);
        }
        Collections.sort(completions);
        return completions;
    }
}

package net.danh.bbm.cmd;

import net.danh.bbm.BBM;
import net.danh.bbm.mythicdrop.MythicXP;
import net.danh.bbm.playerdata.player.PlayerLevel;
import net.danh.bbm.resources.Chat;
import net.danh.bbm.resources.Files;
import net.danh.bbm.resources.Number;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

            if (args.length >= 4 && args.length <= 5) {
                Player t = Bukkit.getPlayer(args[2]);
                String msg = ChatColor.stripColor(args[3]);
                int amount = Number.getInteger(msg);
                if (c.hasPermission("bbm.admin")) {
                    if (t != null) {
                        if (args[0].equalsIgnoreCase("level")) {
                            if (args[1].equalsIgnoreCase("set")) {
                                PlayerLevel.setPlayerLevel(t, amount);
                                if (args.length == 4) {
                                    c.sendMessage(Chat.normalColorize("&aAction: Set; Type: Level; Player: " + t.getName() + "; Number: " + amount));
                                }
                            }
                            if (args[1].equalsIgnoreCase("add")) {
                                PlayerLevel.addPlayerLevel(t, amount);
                                if (args.length == 4) {
                                    c.sendMessage(Chat.normalColorize("&aAction: Add; Type: Level; Player: " + t.getName() + "; Number: " + amount));
                                }
                            }
                            if (args[1].equalsIgnoreCase("remove")) {
                                PlayerLevel.removePlayerEXP(t, amount);
                                if (args.length == 4) {
                                    c.sendMessage(Chat.normalColorize("&aAction: Remove; Type: Level; Player: " + t.getName() + "; Number: " + amount));
                                }
                            }
                        }
                        if (args[0].equalsIgnoreCase("xp")) {
                            if (args[1].equalsIgnoreCase("set")) {
                                PlayerLevel.setPlayerEXP(t, amount);
                                if (args.length == 4) {
                                    c.sendMessage(Chat.normalColorize("&aAction: Set; Type: EXP; Player: " + t.getName() + "; Number: " + amount));
                                }
                            }
                            if (args[1].equalsIgnoreCase("add")) {
                                PlayerLevel.addPlayerEXP(t, amount);
                                if (args.length == 4) {
                                    c.sendMessage(Chat.normalColorize("&aAction: Add; Type: EXP; Player: " + t.getName() + "; Number: " + amount));
                                }
                            }
                            if (args[1].equalsIgnoreCase("remove")) {
                                PlayerLevel.removePlayerEXP(t, amount);
                                if (args.length == 4) {
                                    c.sendMessage(Chat.normalColorize("&aAction: Remove; Type: EXP; Player: " + t.getName() + "; Number: " + amount));
                                }
                            }
                        }
                    }
                }
            }
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("help")) {
                if (c.hasPermission("bbm.admin")) {
                    Files.getMessage().getStringList("admin.help").forEach(s -> c.sendMessage(Chat.colorize(s)));
                }
                Files.getMessage().getStringList("user.help").forEach(s -> c.sendMessage(Chat.colorize(s)));
            }
            if (c instanceof Player p) {
                if (args[0].equalsIgnoreCase("booster")) {
                    if (MythicXP.booster.get(p) > 1d || (MythicXP.booster_temporary_times.get(p) > 0 && MythicXP.booster_temporary_multi.get(p) > 1d)) {
                        Files.getMessage().getStringList("user.booster.active_booster").forEach(s -> c.sendMessage(Chat.colorize((s
                                .replace("<p_multi>", String.valueOf(MythicXP.booster.get(p)))
                                .replace("<t_multi>", String.valueOf(MythicXP.booster_temporary_multi.get(p)))
                                .replace("<t_times>", Number.getTime(MythicXP.booster_temporary_times.get(p)))
                                .replace("<player>", c.getName())))));
                    } else {
                        c.sendMessage(Chat.colorize(Files.getMessage().getString("user.booster.no_booster")));
                    }
                }
            }
        }
        if (args.length >= 3 && args.length <= 5) {
            if (c.hasPermission("bbm.admin")) {
                if (args[0].equalsIgnoreCase("booster")) {
                    if (args.length == 3) {
                        double booster_multi = Number.getDouble(args[2]);
                        double b_multi = Double.parseDouble(new DecimalFormat("#.##").format(booster_multi).replace(",", "."));
                        if (booster_multi > 0d) {
                            Player p = Bukkit.getPlayer(args[1]);
                            if (p != null) {
                                MythicXP.booster.replace(p, b_multi);
                                c.sendMessage(Chat.colorize(Objects.requireNonNull(Files.getMessage().getString("admin.booster.add_booster"))
                                        .replace("<multi>", String.valueOf(b_multi))
                                        .replace("<player>", p.getName())));
                                Files.getMessage().getStringList("user.booster.active_booster").forEach(s -> p.sendMessage(Chat.colorize((s
                                        .replace("<p_multi>", String.valueOf(MythicXP.booster.get(p)))
                                        .replace("<t_multi>", String.valueOf(MythicXP.booster_temporary_multi.get(p)))
                                        .replace("<t_times>", Number.getTime(MythicXP.booster_temporary_times.get(p)))
                                        .replace("<player>", c.getName())))));
                            } else {
                                c.sendMessage(Chat.colorize(Files.getMessage().getString("admin.player_is_null")));
                            }
                        } else {
                            c.sendMessage(Chat.colorize(Files.getMessage().getString("admin.booster.above_zero")));
                        }
                    } else if (args.length == 4) {
                        double booster_multi = Number.getDouble(args[2]);
                        int booster_times = Number.getInteger(args[3]) * 60;
                        double b_multi = Double.parseDouble(new DecimalFormat("#.##").format(booster_multi).replace(",", "."));
                        if (booster_multi > 0d) {
                            Player p = Bukkit.getPlayer(args[1]);
                            if (p != null) {
                                MythicXP.booster_temporary_times.replace(p, booster_times);
                                MythicXP.booster_temporary_multi.replace(p, booster_multi);
                                c.sendMessage(Chat.colorize(Objects.requireNonNull(Files.getMessage().getString("admin.booster.add_booster"))
                                        .replace("<multi>", String.valueOf(b_multi))
                                        .replace("<player>", p.getName())));
                                Files.getMessage().getStringList("user.booster.active_booster").forEach(s -> p.sendMessage(Chat.colorize((s
                                        .replace("<p_multi>", String.valueOf(MythicXP.booster.get(p)))
                                        .replace("<t_multi>", String.valueOf(MythicXP.booster_temporary_multi.get(p)))
                                        .replace("<t_times>", Number.getTime(MythicXP.booster_temporary_times.get(p)))
                                        .replace("<player>", c.getName())))));
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        for (Player p : Bukkit.getOnlinePlayers()) {
                                            if (p.isOnline()) {
                                                if (MythicXP.booster_temporary_times.containsKey(p) && MythicXP.booster_temporary_multi.containsKey(p)) {
                                                    if (MythicXP.booster_temporary_times.get(p) > 0) {
                                                        if (MythicXP.booster_temporary_multi.get(p) > 1.0) {
                                                            int times = MythicXP.booster_temporary_times.get(p);
                                                            if (Math.abs(times) > 0) {
                                                                MythicXP.booster_temporary_times.replace(p, --times);
                                                                if (Math.abs(times) == 0) {
                                                                    p.sendMessage(Chat.colorize(Objects.requireNonNull(Files.getMessage().getString("user.booster.expired_booster"))
                                                                            .replace("<t_multi>", String.valueOf(MythicXP.booster_temporary_multi.get(p)))));
                                                                    MythicXP.booster_temporary_multi.replace(p, 1.0);
                                                                    MythicXP.booster_temporary_times.replace(p, 0);
                                                                    cancel();
                                                                }
                                                            } else {
                                                                p.sendMessage(Chat.colorize(Objects.requireNonNull(Files.getMessage().getString("user.booster.expired_booster"))
                                                                        .replace("<t_multi>", String.valueOf(MythicXP.booster_temporary_multi.get(p)))));
                                                                MythicXP.booster_temporary_multi.replace(p, 1.0);
                                                                MythicXP.booster_temporary_times.replace(p, 0);
                                                                cancel();
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }.runTaskTimer(BBM.getBBMCore(), 20L, 20L);
                            } else {
                                c.sendMessage(Chat.colorize(Files.getMessage().getString("admin.player_is_null")));
                            }
                        } else {
                            c.sendMessage(Chat.colorize(Files.getMessage().getString("admin.booster.above_zero")));
                        }
                    }
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
                commands.add("level");
                commands.add("xp");
                commands.add("reload");
            }
            commands.add("booster");
            commands.add("help");
        }
        StringUtil.copyPartialMatches(args[0], commands, completions);
        if (args.length == 2) {
            if (sender.hasPermission("bbm.admin")) {
                if (args[0].equalsIgnoreCase("booster")) {
                    Bukkit.getOnlinePlayers().forEach(player -> commands.add(player.getName()));
                }
                if (args[0].equalsIgnoreCase("level")
                        || args[0].equalsIgnoreCase("xp")) {
                    commands.add("add");
                    commands.add("set");
                    commands.add("remove");
                }
            }
            StringUtil.copyPartialMatches(args[1], commands, completions);
        }
        if (args.length == 3) {
            if (sender.hasPermission("bbm.admin")) {
                if (args[0].equalsIgnoreCase("booster")) {
                    if (Bukkit.getPlayer(args[1]) != null) {
                        for (double i = 1.5; i <= 5; i += 0.5) {
                            commands.add(String.valueOf(i));
                        }
                    }
                }
                if (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("set")) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        commands.add(p.getName());
                    }
                }
            }
            StringUtil.copyPartialMatches(args[2], commands, completions);
        }
        if (args.length == 4) {
            if (sender.hasPermission("mcoreaddon.admin")) {
                if (args[0].equalsIgnoreCase("booster")) {
                    if (Bukkit.getPlayer(args[1]) != null) {
                        commands.add("<minutes>");
                    }
                }
            }
            StringUtil.copyPartialMatches(args[3], commands, completions);
        }
        Collections.sort(completions);
        return completions;
    }
}

package net.nighthawkempires.permissions.scoreboard;

import net.nighthawkempires.core.NECore;
import net.nighthawkempires.core.language.Lang;
import net.nighthawkempires.core.scoreboard.Scoreboards;
import net.nighthawkempires.permissions.NEPermissions;
import net.nighthawkempires.permissions.user.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class GroupScoreboards extends Scoreboards {

    private int task;

    public String getName() {
        return "group";
    }

    public int getTaskID() {
        return task;
    }

    public int getNumber() {
        return 3;
    }

    public Scoreboard getFor(Player player) {
        Scoreboard[] scoreboard = {Bukkit.getScoreboardManager().getNewScoreboard()};
        final Objective[] objective = {scoreboard[0].registerNewObjective("test", "dummy")};
        objective[0].setDisplaySlot(DisplaySlot.SIDEBAR);
        objective[0].setDisplayName(Lang.SCOREBOARD.getServerBoard());
        UserModel user = NEPermissions.getUserRegistry().getUser(player.getUniqueId());
        Team group = scoreboard[0].registerNewTeam("group");
        group.addEntry(ChatColor.GRAY + " ➛   " + ChatColor.GRAY + "" + ChatColor.BOLD);
        group.setPrefix("");
        group.setSuffix("");
        Team status = scoreboard[0].registerNewTeam("status");
        status.addEntry(ChatColor.GRAY + " ➛   " + ChatColor.DARK_AQUA + "" + ChatColor.BOLD);
        status.setPrefix("");
        status.setSuffix("");
        Team donor = scoreboard[0].registerNewTeam("donor");
        donor.addEntry(ChatColor.GRAY + " ➛   " + ChatColor.RED + "" + ChatColor.BOLD);
        donor.setPrefix("");
        donor.setSuffix("");

        objective[0].getScore(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "" + ChatColor.BOLD + "------------").setScore(10);
        objective[0].getScore(ChatColor.GRAY + "" + ChatColor.BOLD + " Group" + ChatColor.GRAY + ": ").setScore(9);
        objective[0].getScore(ChatColor.GRAY + " ➛   " + ChatColor.GRAY + "" + ChatColor.BOLD).setScore(8);
        group.setSuffix(ChatColor.translateAlternateColorCodes('&', user.getGroup().getPrefix().substring(0, 2) + "&l" + user.getGroup().getName()));
        objective[0].getScore(ChatColor.DARK_PURPLE + " ").setScore(7);
        objective[0].getScore(ChatColor.GRAY + "" + ChatColor.BOLD + " Status" + ChatColor.GRAY + ": ").setScore(6);
        objective[0].getScore(ChatColor.GRAY + " ➛   " + ChatColor.DARK_AQUA + "" + ChatColor.BOLD).setScore(5);
        if (user.getStatus() == null) {
            status.setSuffix(ChatColor.GRAY + "" + ChatColor.BOLD + "NaN");
        } else {
            status.setSuffix(ChatColor.translateAlternateColorCodes('&', user.getStatus().getPrefix().substring(0, 2) + "&l" + user.getStatus().getName()));
        }
        objective[0].getScore(ChatColor.YELLOW + "  ").setScore(4);
        objective[0].getScore(ChatColor.GRAY + "" + ChatColor.BOLD + " Donor Rank" + ChatColor.GRAY + ": ").setScore(3);
        objective[0].getScore(ChatColor.GRAY + " ➛   " + ChatColor.RED + "" + ChatColor.BOLD).setScore(2);
        if (user.getDonor() == null) {
            donor.setSuffix(ChatColor.GRAY + "" + ChatColor.BOLD + "NaN");
        } else {
            donor.setSuffix(ChatColor.translateAlternateColorCodes('&', user.getDonor().getPrefix().substring(0, 2) + "&l" + user.getDonor().getName()));
        }
        objective[0].getScore(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "" + ChatColor.BOLD + "-----------").setScore(1);

        this.task = Bukkit.getScheduler().scheduleSyncRepeatingTask(NECore.getPlugin(), () -> {
            group.setSuffix(ChatColor.translateAlternateColorCodes('&', user.getGroup().getPrefix().substring(0, 2) + "&l" + user.getGroup().getName()));
            if (user.getStatus() == null) {
                status.setSuffix(ChatColor.GRAY + "" + ChatColor.BOLD + "NaN");
            } else {
                status.setSuffix(ChatColor.translateAlternateColorCodes('&', user.getStatus().getPrefix().substring(0, 2) + "&l" + user.getStatus().getName()));
            }
            if (user.getDonor() == null) {
                donor.setSuffix(ChatColor.GRAY + "" + ChatColor.BOLD + "NaN");
            } else {
                donor.setSuffix(ChatColor.translateAlternateColorCodes('&', user.getDonor().getPrefix().substring(0, 2) + "&l" + user.getDonor().getName()));
            }
        }, 0, 5);
        Bukkit.getScheduler().scheduleSyncDelayedTask(NECore.getPlugin(), () -> Bukkit.getScheduler().cancelTask(this.task), 14*20);
        return scoreboard[0];
    }
}

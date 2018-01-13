package net.nighthawkempires.permissions.command;

import net.nighthawkempires.core.language.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DemoteCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("ne.groups")) {
                player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + Lang.NO_PERM.getMessage());
                return true;
            }

            if (args.length == 0) {
                player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.AQUA + "/demote " + ChatColor.DARK_AQUA + "[player]");
                return true;
            } else if (args.length == 1) {
                Bukkit.dispatchCommand(player, "groups demote " + args[0]);
                return true;
            }
        }
        return true;
    }
}

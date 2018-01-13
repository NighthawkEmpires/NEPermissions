package net.nighthawkempires.permissions.command;

import net.nighthawkempires.core.language.Lang;
import net.nighthawkempires.permissions.NEPermissions;
import net.nighthawkempires.permissions.status.Status;
import net.nighthawkempires.permissions.user.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatusCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UserModel user = NEPermissions.getUserRegistry().getUser(player.getUniqueId());

            if (!player.hasPermission("ne.status")) {
                player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + Lang.NO_PERM.getMessage());
                return true;
            }

            if (args.length == 0) {
                String[] help = new String[] {
                        Lang.HEADER.getServerHeader(),
                        Lang.CMD_NAME.getCommandName("status"),
                        Lang.FOOTER.getMessage(),
                        Lang.CMD_HELP.getCommand("status", "help", "Show this menu"),
                        Lang.CMD_HELP.getCommand("status", "list", "List all statuses"),
                        Lang.CMD_HELP.getCommand("status", "remove", "Remove your status"),
                        Lang.CMD_HELP.getCommand("status", "view", "View your status"),
                        Lang.CMD_HELP.getCommand("status", "remove [player]", "Remove a player's status"),
                        Lang.CMD_HELP.getCommand("status", "set [status]", "Set your status"),
                        Lang.CMD_HELP.getCommand("status", "view [player]", "Show a player's status"),
                        Lang.CMD_HELP.getCommand("status", "set [player] [status]", "Set a player's status"),
                        Lang.FOOTER.getMessage()
                };
                player.sendMessage(help);
                return true;
            } else if (args.length == 1) {
                if (args[0].toLowerCase().equals("help")) {
                    String[] help = new String[] {
                            Lang.HEADER.getServerHeader(),
                            Lang.CMD_NAME.getCommandName("status"),
                            Lang.FOOTER.getMessage(),
                            Lang.CMD_HELP.getCommand("status", "help", "Show this menu"),
                            Lang.CMD_HELP.getCommand("status", "list", "List all statuses"),
                            Lang.CMD_HELP.getCommand("status", "remove", "Remove your status"),
                            Lang.CMD_HELP.getCommand("status", "view", "View your status"),
                            Lang.CMD_HELP.getCommand("status", "remove [player]", "Remove a player's status"),
                            Lang.CMD_HELP.getCommand("status", "set [status]", "Set your status"),
                            Lang.CMD_HELP.getCommand("status", "view [player]", "Show a player's status"),
                            Lang.CMD_HELP.getCommand("status", "set [player] [status]", "Set a player's status"),
                            Lang.FOOTER.getMessage()
                    };
                    player.sendMessage(help);
                    return true;
                } else if (args[0].toLowerCase().equals("list")) {
                    String[] list = new String[] {
                            Lang.HEADER.getServerHeader(),
                            Lang.LIST.getListName("Status"),
                            Lang.FOOTER.getMessage(),
                    };
                    player.sendMessage(list);
                    for (Status status : NEPermissions.getStatusManager().getStatuses()) {
                        player.sendMessage(ChatColor.DARK_GRAY + " - " + ChatColor.translateAlternateColorCodes('&', status.getPrefix().substring(0, 2) + status.getName()));
                    }
                    player.sendMessage(Lang.FOOTER.getMessage());
                    return true;
                } else if (args[0].toLowerCase().equals("remove")) {
                    if (user.getStatus() == null) {
                        player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.RED + "You do not have a set status to remove!");
                        return true;
                    }

                    user.setStatus(null);
                    player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.GRAY + "You removed your status");
                    return true;
                } else if (args[0].toLowerCase().equals("view")) {
                    if (user.getStatus() == null) {
                        player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.RED + "You do not have a set status!");
                        return true;
                    }

                    player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.GRAY + "Your set status is " + ChatColor.translateAlternateColorCodes('&',
                            user.getStatus().getPrefix().substring(0, 2) + user.getStatus().getName()) + ChatColor.GRAY + ".");
                    return true;
                } else {
                    player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + Lang.SYNTAX_ERROR.getMessage());
                    return true;
                }
            } else if (args.length == 2) {
                if (args[0].toLowerCase().equals("remove")) {
                    String name = args[1];
                    if (!NEPermissions.getUserRegistry().userExists(Bukkit.getOfflinePlayer(name).getUniqueId())) {
                        player.sendMessage(Lang.PLAYER_NULL.getServerChatTag() + Lang.PLAYER_NULL.getMessage());
                        return true;
                    }

                    if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(name))) {
                        Player target = Bukkit.getPlayer(name);
                        if (!NEPermissions.getUserRegistry().getRegisteredData().containsKey(target.getUniqueId().toString())) {
                            NEPermissions.getUserRegistry().loadFromDb(target.getUniqueId().toString());
                        }
                        UserModel tuser = NEPermissions.getUserRegistry().getUser(target.getUniqueId());

                        tuser.setStatus(null);
                        player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.GRAY + "You have removed " + ChatColor.BLUE + target.getName() + "'s " + ChatColor.GRAY + " status.");
                        target.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.GRAY + "Your status has been removed.");
                        return true;
                    } else {
                        OfflinePlayer target = Bukkit.getOfflinePlayer(name);
                        if (!NEPermissions.getUserRegistry().getRegisteredData().containsKey(target.getUniqueId().toString())) {
                            NEPermissions.getUserRegistry().loadFromDb(target.getUniqueId().toString());
                        }
                        UserModel tuser = NEPermissions.getUserRegistry().getUser(target.getUniqueId());

                        tuser.setStatus(null);
                        player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.GRAY + "You have removed " + ChatColor.BLUE + target.getName() + "'s " + ChatColor.GRAY + " status.");
                    }
                    return true;
                } else if (args[0].toLowerCase().equals("set")) {
                    String name = args[1];
                    if (!NEPermissions.getStatusManager().exists(name)) {
                        player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.RED + "That status does not exist!");
                        return true;
                    }

                    Status status = NEPermissions.getStatusManager().getStatus(name);
                    user.setStatus(status);
                    player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.GRAY + "You have set your status to "
                            + ChatColor.translateAlternateColorCodes('&', status.getPrefix().substring(0, 2) + status.getName()) + ChatColor.GRAY + ".");
                    return true;
                } else if (args[0].toLowerCase().equals("view")) {
                    String name = args[1];
                    if (!NEPermissions.getUserRegistry().userExists(Bukkit.getOfflinePlayer(name).getUniqueId())) {
                        player.sendMessage(Lang.PLAYER_NULL.getServerChatTag() + Lang.PLAYER_NULL.getMessage());
                        return true;
                    }

                    OfflinePlayer target = Bukkit.getOfflinePlayer(name);
                    if (!NEPermissions.getUserRegistry().getRegisteredData().containsKey(target.getUniqueId().toString())) {
                        NEPermissions.getUserRegistry().loadFromDb(target.getUniqueId().toString());
                    }
                    UserModel tuser = NEPermissions.getUserRegistry().getUser(target.getUniqueId());                    if (tuser.getStatus() == null) {
                        player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.RED + "That player does not have a status set!");
                        return true;
                    }
                    Status status = tuser.getStatus();
                    player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.BLUE + Bukkit.getOfflinePlayer(name).getName() + "'s " + ChatColor.GRAY + "status is set to "
                            + ChatColor.translateAlternateColorCodes('&', status.getPrefix().substring(0, 2) + status.getName()) + ChatColor.GRAY + ".");

                } else {
                    player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + Lang.SYNTAX_ERROR.getMessage());
                    return true;
                }
            } else if (args.length == 3) {
                if (args[0].toLowerCase().equals("set")) {
                    String name = args[1];
                    String sname = args[2];
                    if (!NEPermissions.getUserRegistry().userExists(Bukkit.getOfflinePlayer(name).getUniqueId())) {
                        player.sendMessage(Lang.PLAYER_NULL.getServerChatTag() + Lang.PLAYER_NULL.getMessage());
                        return true;
                    }


                    if (!NEPermissions.getStatusManager().exists(sname)) {
                        player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.RED + "That status does not exist!");
                        return true;
                    }

                    Status status = NEPermissions.getStatusManager().getStatus(sname);
                    if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(name))) {
                        Player target = Bukkit.getPlayer(name);
                        if (!NEPermissions.getUserRegistry().getRegisteredData().containsKey(target.getUniqueId().toString())) {
                            NEPermissions.getUserRegistry().loadFromDb(target.getUniqueId().toString());
                        }
                        UserModel tuser = NEPermissions.getUserRegistry().getUser(target.getUniqueId());

                        tuser.setStatus(status);
                        player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.GRAY + "You have set " + ChatColor.BLUE + target.getName() + "'s " + ChatColor.GRAY + " to "
                                + ChatColor.translateAlternateColorCodes('&', status.getPrefix().substring(0, 2) + status.getName()) + ChatColor.GRAY + ".");
                        target.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.GRAY + "Your status has been set to "
                                + ChatColor.translateAlternateColorCodes('&', status.getPrefix().substring(0, 2) + status.getName()) + ChatColor.GRAY + ".");
                        return true;
                    } else {
                        OfflinePlayer target = Bukkit.getOfflinePlayer(name);
                        if (!NEPermissions.getUserRegistry().getRegisteredData().containsKey(target.getUniqueId().toString())) {
                            NEPermissions.getUserRegistry().loadFromDb(target.getUniqueId().toString());
                        }
                        UserModel tuser = NEPermissions.getUserRegistry().getUser(target.getUniqueId());

                        tuser.setStatus(status);
                        player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.GRAY + "You have set " + ChatColor.BLUE + target.getName() + "'s " + ChatColor.GRAY + " to "
                                + ChatColor.translateAlternateColorCodes('&', status.getPrefix().substring(0, 2) + status.getName()) + ChatColor.GRAY + ".");
                    }
                } else {
                    player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + Lang.SYNTAX_ERROR.getMessage());
                    return true;
                }
            } else {
                player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + Lang.SYNTAX_ERROR.getMessage());
                return true;
            }
        }
        return true;
    }
}
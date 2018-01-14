package net.nighthawkempires.permissions.command;

import net.nighthawkempires.core.language.Lang;
import net.nighthawkempires.permissions.NEPermissions;
import net.nighthawkempires.permissions.donor.Donor;
import net.nighthawkempires.permissions.event.DonorChangeEvent;
import net.nighthawkempires.permissions.user.UserModel;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import static net.nighthawkempires.permissions.NEPermissions.getDonorManager;
import static net.nighthawkempires.permissions.NEPermissions.getPluginManager;

public class DonorCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UserModel user = NEPermissions.getUserRegistry().getUser(player.getUniqueId());

            if (!player.hasPermission("ne.donors")) {
                player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + Lang.NO_PERM.getMessage());
                return true;
            }

            if (args.length == 0) {
                String[] help = new String[]{
                        Lang.HEADER.getServerHeader(),
                        Lang.CMD_NAME.getCommandName("donor"),
                        Lang.FOOTER.getMessage(),
                        Lang.CMD_HELP.getCommand("donor", "help", "Show this menu"),
                        Lang.CMD_HELP.getCommand("donor", "info", "Show your donor rank info"),
                        Lang.CMD_HELP.getCommand("donor", "list", "List all donor ranks"),
                        Lang.CMD_HELP.getCommand("donor", "remove", "Remove your donor rank"),
                        Lang.CMD_HELP.getCommand("donor", "view", "View your donor rank"),
                        Lang.CMD_HELP.getCommand("donor", "info [donor]", "Show a donor rank's info"),
                        Lang.CMD_HELP.getCommand("donor", "remove [player]", "Remove a player's donor rank"),
                        Lang.CMD_HELP.getCommand("donor", "set [donor]", "Set your donor rank"),
                        Lang.CMD_HELP.getCommand("donor", "view [player]", "Show a player's donor rank"),
                        Lang.CMD_HELP.getCommand("donor", "set [player] [donor]", "Set a player's donor rank"),
                        Lang.FOOTER.getMessage(),
            };
                player.sendMessage(help);
                return true;
            } else if (args.length == 1) {
                if (args[0].toLowerCase().equals("help")) {
                    String[] help = new String[]{
                            Lang.HEADER.getServerHeader(),
                            Lang.CMD_NAME.getCommandName("donor"),
                            Lang.FOOTER.getMessage(),
                            Lang.CMD_HELP.getCommand("donor", "help", "Show this menu"),
                            Lang.CMD_HELP.getCommand("donor", "info", "Show your donor rank info"),
                            Lang.CMD_HELP.getCommand("donor", "list", "List all donor ranks"),
                            Lang.CMD_HELP.getCommand("donor", "remove", "Remove your donor rank"),
                            Lang.CMD_HELP.getCommand("donor", "view", "View your donor rank"),
                            Lang.CMD_HELP.getCommand("donor", "info [donor]", "Show a donor rank's info"),
                            Lang.CMD_HELP.getCommand("donor", "remove [player]", "Remove a player's donor rank"),
                            Lang.CMD_HELP.getCommand("donor", "set [donor]", "Set your donor rank"),
                            Lang.CMD_HELP.getCommand("donor", "view [player]", "Show a player's donor rank"),
                            Lang.CMD_HELP.getCommand("donor", "set [player] [donor]", "Set a player's donor rank"),
                            Lang.FOOTER.getMessage(),
                    };
                    player.sendMessage(help);
                    return true;
                } else if (args[0].toLowerCase().equals("info")) {
                    if (user.getDonor() == null) {
                        player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.GRAY + "You do not have a set donor rank.");
                        return true;
                    }
                    StringBuilder perms = new StringBuilder();
                    ChatColor color = ChatColor.GREEN;
                    for (String perm : user.getDonor().getPermissions()) {
                        String permission = perm;
                        if (perm.startsWith("-")) {
                            color = ChatColor.RED;
                            permission = permission.substring(1);
                        }
                        perms.append(color).append(permission).append(ChatColor.DARK_GRAY).append(", ");
                    }
                    String[] info = new String[] {
                            Lang.HEADER.getServerHeader(),
                            Lang.INFO.getInfo(user.getDonor().getName()),
                            Lang.FOOTER.getMessage(),
                            ChatColor.DARK_GRAY + "Name" + ChatColor.GRAY + ": " + ChatColor.translateAlternateColorCodes('&', user.getDonor().getPrefix().substring(0, 2)) + user.getDonor().getName(),
                            ChatColor.DARK_GRAY + "Prefix" + ChatColor.GRAY + ": " + ChatColor.DARK_GRAY + "[" + ChatColor.translateAlternateColorCodes('&', user.getDonor().getPrefix()) + ChatColor.DARK_GRAY + "]",
                            ChatColor.DARK_GRAY + "Permissions" + ChatColor.GRAY + ": " + perms.toString().substring(0, perms.length() -2),
                            Lang.FOOTER.getMessage()
                    };
                    player.sendMessage(info);
                    return true;
                } else if (args[0].toLowerCase().equals("list")) {
                    String[] list = new String[]{
                            Lang.HEADER.getServerHeader(),
                            Lang.LIST.getListName("Donor"),
                            Lang.FOOTER.getMessage(),
                    };
                    player.sendMessage(list);
                    for (Donor donor : NEPermissions.getDonorManager().getDonors()) {
                        player.sendMessage(ChatColor.DARK_GRAY + " - " + ChatColor.translateAlternateColorCodes('&', donor.getPrefix().substring(0, 2) + donor.getName()));
                    }
                    player.sendMessage(Lang.FOOTER.getMessage());
                    return true;
                } else if (args[0].toLowerCase().equals("remove")) {
                    if (user.getDonor() == null) {
                        player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.RED + "You do not have a set donor rank to remove!");
                        return true;
                    }

                    user.setDonor(null);
                    NEPermissions.getPluginManager().callEvent(new DonorChangeEvent(player));
                    player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.GRAY + "You removed your donor rank");
                    return true;
                } else if (args[0].toLowerCase().equals("view")) {
                    if (user.getDonor() == null) {
                        player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.RED + "You do not have a set donor rank!");
                        return true;
                    }

                    player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.GRAY + "Your set donor rank is " + ChatColor.translateAlternateColorCodes('&',
                            user.getDonor().getPrefix().substring(0, 2) + user.getDonor().getName()) + ChatColor.GRAY + ".");
                    return true;
                } else {
                    player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + Lang.SYNTAX_ERROR.getMessage());
                    return true;
                }
            } else if (args.length == 2) {
                if (args[0].toLowerCase().equals("info")) {
                    String name = args[1];
                    if (!getDonorManager().exists(name)) {
                        player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.RED + "That donor rank does not exist.");
                        return true;
                    }

                    Donor donor = getDonorManager().getDonor(name);
                    StringBuilder perms = new StringBuilder();
                    ChatColor color = ChatColor.GREEN;
                    for (String perm : donor.getPermissions()) {
                        String permission = perm;
                        if (perm.startsWith("-")) {
                            color = ChatColor.RED;
                            permission = permission.substring(1);
                        }
                        perms.append(color).append(permission).append(ChatColor.DARK_GRAY).append(", ");
                    }
                    String[] info = new String[] {
                            Lang.HEADER.getServerHeader(),
                            Lang.INFO.getInfo(donor.getName()),
                            Lang.FOOTER.getMessage(),
                            ChatColor.DARK_GRAY + "Name" + ChatColor.GRAY + ": " + ChatColor.translateAlternateColorCodes('&', donor.getPrefix().substring(0, 2)) + donor.getName(),
                            ChatColor.DARK_GRAY + "Prefix" + ChatColor.GRAY + ": " + ChatColor.DARK_GRAY + "[" + ChatColor.translateAlternateColorCodes('&', donor.getPrefix()) + ChatColor.DARK_GRAY + "]",
                            ChatColor.DARK_GRAY + "Permissions" + ChatColor.GRAY + ": " + perms.toString().substring(0, perms.length() -2),
                            Lang.FOOTER.getMessage()
                    };
                    player.sendMessage(info);
                    return true;
                }else if (args[0].toLowerCase().equals("remove")) {
                    String name = args[1];
                    if (!NEPermissions.getUserRegistry().userExists(Bukkit.getOfflinePlayer(name).getUniqueId())) {
                        player.sendMessage(Lang.PLAYER_NULL.getServerChatTag() + Lang.PLAYER_NULL.getMessage());
                        return true;
                    }

                    if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(name))) {
                        Player target = Bukkit.getPlayer(name);
                        UserModel tuser = NEPermissions.getUserRegistry().getUser(target.getUniqueId());

                        tuser.setDonor(null);
                        NEPermissions.getPluginManager().callEvent(new DonorChangeEvent(target));
                        player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.GRAY + "You have removed " + ChatColor.BLUE + target.getName() + "'s " + ChatColor.GRAY + " donor rank.");
                        target.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.GRAY + "Your donor rank has been removed.");
                        return true;
                    } else {
                        OfflinePlayer target = Bukkit.getOfflinePlayer(name);
                        UserModel tuser = NEPermissions.getUserRegistry().getUser(target.getUniqueId());

                        tuser.setDonor(null);
                        player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.GRAY + "You have removed " + ChatColor.BLUE + target.getName() + "'s " + ChatColor.GRAY + " donor rank.");
                    }
                    return true;
                } else if (args[0].toLowerCase().equals("set")) {
                    String name = args[1];
                    if (!NEPermissions.getDonorManager().exists(name)) {
                        player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.RED + "That donor rank does not exist!");
                        return true;
                    }

                    Donor donor = NEPermissions.getDonorManager().getDonor(name);
                    user.setDonor(donor);
                    NEPermissions.getPluginManager().callEvent(new DonorChangeEvent(player));
                    player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.GRAY + "You have set your donor rank to "
                            + ChatColor.translateAlternateColorCodes('&', donor.getPrefix().substring(0, 2) + donor.getName()) + ChatColor.GRAY + ".");
                    return true;
                } else if (args[0].toLowerCase().equals("view")) {
                    String name = args[1];
                    if (!NEPermissions.getUserRegistry().userExists(Bukkit.getOfflinePlayer(name).getUniqueId())) {
                        player.sendMessage(Lang.PLAYER_NULL.getServerChatTag() + Lang.PLAYER_NULL.getMessage());
                        return true;
                    }

                    OfflinePlayer target = Bukkit.getOfflinePlayer(name);
                    UserModel tuser = NEPermissions.getUserRegistry().getUser(target.getUniqueId());
                    if (tuser.getDonor() == null) {
                        player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.RED + "That player does not have a donor rank set!");
                        return true;
                    }
                    Donor donor = tuser.getDonor();
                    player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.BLUE + Bukkit.getOfflinePlayer(name).getName() + "'s " + ChatColor.GRAY + "donor rank is set to "
                            + ChatColor.translateAlternateColorCodes('&', donor.getPrefix().substring(0, 2) + donor.getName()) + ChatColor.GRAY + ".");

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

                    if (!NEPermissions.getDonorManager().exists(sname)) {
                        player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.RED + "That donor rank does not exist!");
                        return true;
                    }

                    Donor donor = NEPermissions.getDonorManager().getDonor(sname);
                    if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(name))) {
                        Player target = Bukkit.getPlayer(name);
                        UserModel tuser = NEPermissions.getUserRegistry().getUser(target.getUniqueId());

                        tuser.setDonor(donor);
                        getPluginManager().callEvent(new DonorChangeEvent(target));
                        player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.GRAY + "You have set " + ChatColor.BLUE + target.getName() + "'s " + ChatColor.GRAY + " donor rank to "
                                + ChatColor.translateAlternateColorCodes('&', donor.getPrefix().substring(0, 2) + donor.getName()) + ChatColor.GRAY + ".");
                        target.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.GRAY + "Your donor rank has been set to "
                                + ChatColor.translateAlternateColorCodes('&', donor.getPrefix().substring(0, 2) + donor.getName()) + ChatColor.GRAY + ".");
                        return true;
                    } else {
                        OfflinePlayer target = Bukkit.getOfflinePlayer(name);
                        UserModel tuser = NEPermissions.getUserRegistry().getUser(target.getUniqueId());

                        tuser.setDonor(donor);
                        player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.GRAY + "You have set " + ChatColor.BLUE + target.getName() + "'s " + ChatColor.GRAY + " donor rank to "
                                + ChatColor.translateAlternateColorCodes('&', donor.getPrefix().substring(0, 2) + donor.getName()) + ChatColor.GRAY + ".");
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
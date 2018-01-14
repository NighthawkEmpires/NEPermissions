package net.nighthawkempires.permissions.command;

import net.nighthawkempires.core.language.Lang;
import net.nighthawkempires.permissions.NEPermissions;
import net.nighthawkempires.permissions.donor.Donor;
import net.nighthawkempires.permissions.group.Group;
import net.nighthawkempires.permissions.user.UserModel;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import static net.nighthawkempires.permissions.NEPermissions.*;

public class PermissionsCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UserModel user = NEPermissions.getUserRegistry().getUser(player.getUniqueId());

            if (!player.hasPermission("ne.permissions")) {
                player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + Lang.NO_PERM.getMessage());
                return true;
            }

            if (args.length == 0) {
                String[] help = new String[]{
                        Lang.HEADER.getServerHeader(),
                        Lang.CMD_NAME.getCommandName("perm"),
                        Lang.FOOTER.getMessage(),
                        Lang.CMD_HELP.getCommand("perm", "help", "Show this menu"),
                        Lang.CMD_HELP.getCommand("perm", "view", "Show your perms"),
                        Lang.CMD_HELP.getCommand("perm", "view [-p|-g|-d] [p|g|d]", "Show a pla/gro/don's perms"),
                        Lang.CMD_HELP.getCommand("perm", "add [-p|-g|-d] [p|g|d] [perm]", "Add a perm to a pla/gro/don"),
                        Lang.CMD_HELP.getCommand("perm", "rem [-p|-g|-d] [p|g|d] [perm]", "Remove a perm from a pla/gro/don"),
                        Lang.FOOTER.getMessage(),
                };
                player.sendMessage(help);
                return true;
            } else if (args.length == 1) {
                if (args[0].toLowerCase().equals("help")) {
                    String[] help = new String[]{
                            Lang.HEADER.getServerHeader(),
                            Lang.CMD_NAME.getCommandName("perm"),
                            Lang.FOOTER.getMessage(),
                            Lang.CMD_HELP.getCommand("perm", "help", "Show this menu"),
                            Lang.CMD_HELP.getCommand("perm", "view", "Show your perms"),
                            Lang.CMD_HELP.getCommand("perm", "view [-p|-g|-d] [p|g|d]", "Show a pla/gro/don's perms"),
                            Lang.CMD_HELP.getCommand("perm", "add [-p|-g|-d] [p|g|d] [perm]", "Add a perm to a pla/gro/don"),
                            Lang.CMD_HELP.getCommand("perm", "rem [-p|-g|-d] [p|g|d] [perm]", "Remove a perm from a pla/gro/don"),
                            Lang.FOOTER.getMessage(),
                    };
                    player.sendMessage(help);
                    return true;
                } else if (args[0].toLowerCase().equals("view")) {
                    StringBuilder play = new StringBuilder();
                    StringBuilder group = new StringBuilder();
                    StringBuilder donor = new StringBuilder();
                    if (!user.getPermissions().isEmpty()) {
                        for (String string : user.getPermissions()) {
                            ChatColor color = ChatColor.GREEN;
                            String perm = string;
                            if (string.startsWith("-")) {
                                color = ChatColor.RED;
                                perm = string.substring(1);
                            }
                            play.append(color).append(perm).append(ChatColor.DARK_GRAY).append(", ");
                        }
                    } else {
                        play.append(ChatColor.GRAY).append("NaNaN");
                    }

                    if (!user.getGroup().getPermissions().isEmpty()) {
                        for (String string : user.getGroup().getPermissions()) {
                            ChatColor color = ChatColor.GREEN;
                            String perm = string;
                            if (string.startsWith("-")) {
                                color = ChatColor.RED;
                                perm = string.substring(1);
                            }
                            group.append(color).append(perm).append(ChatColor.DARK_GRAY).append(", ");
                        }
                    } else {
                        group.append(ChatColor.GRAY).append("NaNaN");
                    }

                    if (user.getDonor() != null) {
                        if (!user.getDonor().getPermissions().isEmpty()) {
                            for (String string : user.getDonor().getPermissions()) {
                                ChatColor color = ChatColor.GREEN;
                                String perm = string;
                                if (string.startsWith("-")) {
                                    color = ChatColor.RED;
                                    perm = string.substring(1);
                                }
                                donor.append(color).append(perm).append(ChatColor.DARK_GRAY).append(", ");
                            }
                        } else {
                            donor.append(ChatColor.GRAY).append("NaNaN");
                        }
                    } else {
                        donor.append(ChatColor.GRAY).append("NaNaN");
                    }
                    String[] info = new String[] {
                            Lang.HEADER.getServerHeader(),
                            Lang.LIST.getListName("Your Permissions"),
                            Lang.FOOTER.getMessage(),
                            ChatColor.DARK_GRAY + "Player Based" + ChatColor.GRAY + ": " + play.substring(0, play.length() -2),
                            ChatColor.DARK_GRAY + "Group Based" + ChatColor.GRAY + ": " + group.substring(0, group.length() - 2),
                            ChatColor.DARK_GRAY + "Donor Rank Based" + ChatColor.GRAY + ": " + donor.substring(0, donor.length() - 2),
                            Lang.FOOTER.getMessage()
                    };
                    player.sendMessage(info);
                    return true;
                } else {
                    player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + Lang.SYNTAX_ERROR.getMessage());
                    return true;
                }
            } else if (args.length == 3) {
                if (args[0].toLowerCase().equals("view")) {
                    if (args[1].toLowerCase().equals("-p") || args[1].toLowerCase().equals("-player")) {
                        String name = args[2];
                        if (!NEPermissions.getUserRegistry().userExists(Bukkit.getOfflinePlayer(name).getUniqueId())) {
                            player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + Lang.PLAYER_NULL.getMessage());
                            return true;
                        }

                        OfflinePlayer target = Bukkit.getOfflinePlayer(name);
                        if (!NEPermissions.getUserRegistry().getRegisteredData().containsKey(target.getUniqueId().toString())) {
                            NEPermissions.getUserRegistry().loadFromDb(target.getUniqueId().toString());
                        }
                        UserModel tuser = NEPermissions.getUserRegistry().getUser(target.getUniqueId());

                        StringBuilder perms = new StringBuilder();
                        if (!tuser.getPermissions().isEmpty()) {
                            for (String string : tuser.getPermissions()) {
                                ChatColor color = ChatColor.GREEN;
                                String perm = string;
                                if (string.startsWith("-")) {
                                    color = ChatColor.RED;
                                    perm = perm.substring(1);
                                }
                                perms.append(color).append(perm).append(ChatColor.DARK_GRAY).append(", ");
                            }
                        } else {
                            perms.append(ChatColor.GRAY).append("NaNaN");
                        }

                        String[] list = new String[] {
                                Lang.HEADER.getServerHeader(),
                                Lang.LIST.getListName(ChatColor.BLUE + Bukkit.getOfflinePlayer(name).getName() + "'s " + ChatColor.GRAY + "Permissions"),
                                Lang.FOOTER.getMessage(),
                                ChatColor.DARK_GRAY + "Permissions" + ChatColor.GRAY + ": " + perms,
                                Lang.FOOTER.getMessage()
                        };
                        player.sendMessage(list);
                    } else if (args[1].toLowerCase().equals("-g") || args[1].toLowerCase().equals("-group")) {
                        String name = args[2];
                        if (!getGroupManager().exists(name)) {
                            player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.RED + "That group does not exist!");
                            return true;
                        }

                        Group group = getGroupManager().getGroup(name);
                        StringBuilder perms = new StringBuilder();
                        if (!group.getPermissions().isEmpty()) {
                            for (String string : group.getPermissions()) {
                                ChatColor color = ChatColor.GREEN;
                                String perm = string;
                                if (string.startsWith("-")) {
                                    color = ChatColor.RED;
                                    perm = perm.substring(1);
                                }
                                perms.append(color).append(perm).append(ChatColor.DARK_GRAY).append(", ");
                            }
                        } else {
                            perms.append(ChatColor.GRAY).append("NaNaN");
                        }

                        String[] list = new String[] {
                                Lang.HEADER.getServerHeader(),
                                Lang.LIST.getListName(ChatColor.translateAlternateColorCodes('&', group.getPrefix().substring(0, 2) + group.getName()) + "'s " + ChatColor.GRAY + "Permissions"),
                                Lang.FOOTER.getMessage(),
                                ChatColor.DARK_GRAY + "Permissions" + ChatColor.GRAY + ": " + perms,
                                Lang.FOOTER.getMessage()
                        };
                        player.sendMessage(list);
                    } else if (args[1].toLowerCase().equals("-d") || args[1].toLowerCase().equals("-donor")) {
                        String name = args[2];
                        if (!getDonorManager().exists(name)) {
                            player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.RED + "That donor rank does not exist!");
                            return true;
                        }

                        Donor donor = getDonorManager().getDonor(name);
                        StringBuilder perms = new StringBuilder();
                        if (!donor.getPermissions().isEmpty()) {
                            for (String string : donor.getPermissions()) {
                                ChatColor color = ChatColor.GREEN;
                                String perm = string;
                                if (string.startsWith("-")) {
                                    color = ChatColor.RED;
                                    perm = perm.substring(1);
                                }
                                perms.append(color).append(perm).append(ChatColor.DARK_GRAY).append(", ");
                            }
                        } else {
                            perms.append(ChatColor.GRAY).append("NaNaN");
                        }

                        String[] list = new String[] {
                                Lang.HEADER.getServerHeader(),
                                Lang.LIST.getListName(ChatColor.translateAlternateColorCodes('&', donor.getPrefix().substring(0, 2) + donor.getName()) + "'s " + ChatColor.GRAY + "Permissions"),
                                Lang.FOOTER.getMessage(),
                                ChatColor.DARK_GRAY + "Permissions" + ChatColor.GRAY + ": " + perms,
                                Lang.FOOTER.getMessage()
                        };
                        player.sendMessage(list);
                    } else {
                        player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + Lang.SYNTAX_ERROR.getMessage());
                        return true;
                    }
                } else {
                    player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + Lang.SYNTAX_ERROR.getMessage());
                    return true;
                }
            } else if (args.length == 4) {
                if (args[0].toLowerCase().equals("add")) {
                    if (args[1].toLowerCase().equals("-p") || args[1].toLowerCase().equals("-player")) {
                        String name = args[2];
                        if (!NEPermissions.getUserRegistry().userExists(Bukkit.getOfflinePlayer(name).getUniqueId())) {
                            player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + Lang.PLAYER_NULL.getMessage());
                            return true;
                        }

                        OfflinePlayer target = Bukkit.getOfflinePlayer(name);
                        if (!NEPermissions.getUserRegistry().getRegisteredData().containsKey(target.getUniqueId().toString())) {
                            NEPermissions.getUserRegistry().loadFromDb(target.getUniqueId().toString());
                        }
                        UserModel tuser = NEPermissions.getUserRegistry().getUser(target.getUniqueId());

                        if (tuser.getPermissions().contains(args[3])) {
                            player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.RED + "That player already has that permission.");
                            return true;
                        }

                        tuser.addPermission(args[3]);
                        if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(name))) {
                            getPermissionManager().setupPermissions(Bukkit.getPlayer(name));
                        }
                        player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.GRAY + "You have added permission " + ChatColor.GREEN + args[3] + ChatColor.GRAY + " to player " + ChatColor.BLUE
                                + Bukkit.getOfflinePlayer(name).getName() + ChatColor.GRAY + ".");
                        return true;
                    } else if (args[1].toLowerCase().equals("-g") || args[1].toLowerCase().equals("-group")) {
                        String name = args[2];
                        if (!getGroupManager().exists(name)) {
                            player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.RED + "That group does not exist!");
                            return true;
                        }

                        Group group = getGroupManager().getGroup(name);
                        if (group.getPermissions().contains(args[3])) {
                            player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.RED + "That group already has that permission.");
                            return true;
                        }

                        group.getPermissions().add(args[3]);
                        NEPermissions.getGroupManager().reloadPerms();
                        for (Player players : Bukkit.getOnlinePlayers()) {
                            UserModel users = NEPermissions.getUserRegistry().getUser(players.getUniqueId());
                            if (users.getGroup().getName().toLowerCase().equals(group.getName().toLowerCase())) {
                                getPermissionManager().addPermission(players, args[3]);
                            }
                        }
                        player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.GRAY + "You have added permission " + ChatColor.GREEN + args[3] + ChatColor.GRAY + " to group "
                                + ChatColor.translateAlternateColorCodes('&', group.getPrefix().substring(0, 2) + group.getName()) + ChatColor.GRAY + ".");
                        return true;
                    } else if (args[1].toLowerCase().equals("-d") || args[1].toLowerCase().equals("-donor")) {
                        String name = args[3];
                        if (!getDonorManager().exists(name)) {
                            player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.RED + "That donor rank does not exist!");
                            return true;
                        }

                        Donor donor = getDonorManager().getDonor(name);
                        if (donor.getPermissions().contains(args[3])) {
                            player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.RED + "That donor rank already has that permission.");
                            return true;
                        }

                        donor.getPermissions().add(args[3]);
                        for (Player players : Bukkit.getOnlinePlayers()) {
                            UserModel users = NEPermissions.getUserRegistry().getUser(players.getUniqueId());
                            if (users.getDonor().getName().toLowerCase().equals(donor.getName().toLowerCase())) {
                                getPermissionManager().addPermission(players, args[3]);
                            }
                        }
                        player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.GRAY + "You have added permission " + ChatColor.GREEN + args[3] + ChatColor.GRAY + " to donor rank "
                                + ChatColor.translateAlternateColorCodes('&', donor.getPrefix().substring(0, 2) + donor.getName()) + ChatColor.GRAY + ".");
                        return true;
                    } else {
                        player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + Lang.SYNTAX_ERROR.getMessage());
                        return true;
                    }
                } else if (args[0].toLowerCase().equals("rem")) {
                    if (args[1].toLowerCase().equals("-p") || args[1].toLowerCase().equals("-player")) {
                        String name = args[2];
                        if (!NEPermissions.getUserRegistry().userExists(Bukkit.getOfflinePlayer(name).getUniqueId())) {
                            player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + Lang.PLAYER_NULL.getMessage());
                            return true;
                        }

                        OfflinePlayer target = Bukkit.getOfflinePlayer(name);
                        UserModel tuser = NEPermissions.getUserRegistry().getUser(target.getUniqueId());

                        if (!tuser.getPermissions().contains(args[3])) {
                            player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.RED + "That player does not have that permission.");
                            return true;
                        }

                        tuser.removePermission(args[3]);
                        if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(name))) {
                            getPermissionManager().removePermission(Bukkit.getPlayer(args[2]), args[3]);
                        }
                        player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.GRAY + "You have removed permission " + ChatColor.RED + args[3] + ChatColor.GRAY + " from player " + ChatColor.BLUE
                                + Bukkit.getOfflinePlayer(name).getName() + ChatColor.GRAY + ".");
                        return true;
                    } else if (args[1].toLowerCase().equals("-g") || args[1].toLowerCase().equals("-group")) {
                        String name = args[2];
                        if (!getGroupManager().exists(name)) {
                            player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.RED + "That group does not exist!");
                            return true;
                        }

                        Group group = getGroupManager().getGroup(name);
                        if (!group.getPermissions().contains(args[3])) {
                            player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.RED + "That group does not have that permission.");
                            return true;
                        }

                        group.getPermissions().remove(args[3]);
                        NEPermissions.getGroupManager().reloadPerms();
                        for (Player players : Bukkit.getOnlinePlayers()) {
                            UserModel users = NEPermissions.getUserRegistry().getUser(players.getUniqueId());
                            if (users.getGroup().getName().toLowerCase().equals(group.getName().toLowerCase())) {
                                getPermissionManager().removePermission(players, args[3]);
                            }
                        }
                        player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.GRAY + "You have removed permission " + ChatColor.RED + args[3] + ChatColor.GRAY + " from group "
                                + ChatColor.translateAlternateColorCodes('&', group.getPrefix().substring(0, 2) + group.getName()) + ChatColor.GRAY + ".");
                        return true;
                    } else if (args[1].toLowerCase().equals("-d") || args[1].toLowerCase().equals("-donor")) {
                        String name = args[3];
                        if (!getDonorManager().exists(name)) {
                            player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.RED + "That donor rank does not exist!");
                            return true;
                        }

                        Donor donor = getDonorManager().getDonor(name);
                        if (!donor.getPermissions().contains(args[3])) {
                            player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.RED + "That donor rank does not have that permission.");
                            return true;
                        }

                        donor.getPermissions().remove(args[3]);
                        for (Player players : Bukkit.getOnlinePlayers()) {
                            UserModel users = NEPermissions.getUserRegistry().getUser(players.getUniqueId());
                            if (users.getDonor().getName().toLowerCase().equals(donor.getName().toLowerCase())) {
                                getPermissionManager().removePermission(players, args[3]);
                            }
                        }
                        player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.GRAY + "You have removed permission " + ChatColor.RED + args[3] + ChatColor.GRAY + " from donor rank "
                                + ChatColor.translateAlternateColorCodes('&', donor.getPrefix().substring(0, 2) + donor.getName()) + ChatColor.GRAY + ".");
                        return true;
                    } else {
                        player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + Lang.SYNTAX_ERROR.getMessage());
                        return true;
                    }
                }
            } else {
                player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + Lang.SYNTAX_ERROR.getMessage());
                return true;
            }
        }
        return true;
    }
}

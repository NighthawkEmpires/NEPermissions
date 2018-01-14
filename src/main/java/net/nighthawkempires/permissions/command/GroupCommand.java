package net.nighthawkempires.permissions.command;

import net.nighthawkempires.core.language.Lang;
import net.nighthawkempires.permissions.NEPermissions;
import net.nighthawkempires.permissions.event.GroupChangeEvent;
import net.nighthawkempires.permissions.group.Group;
import net.nighthawkempires.permissions.user.UserModel;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import static net.nighthawkempires.permissions.NEPermissions.getGroupManager;

public class GroupCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UserModel user = NEPermissions.getUserRegistry().getUser(player.getUniqueId());

            if (!player.hasPermission("ne.groups")) {
                player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + Lang.NO_PERM.getMessage());
                return true;
            }

            if (args.length == 0) {
                String[] help = new String[] {
                        Lang.HEADER.getServerHeader(),
                        Lang.CMD_NAME.getCommandName("group"),
                        Lang.FOOTER.getMessage(),
                        Lang.CMD_HELP.getCommand("group", "help", "Show this menu"),
                        Lang.CMD_HELP.getCommand("group", "info", "Show your group info"),
                        Lang.CMD_HELP.getCommand("group", "list", "List all groups"),
                        Lang.CMD_HELP.getCommand("group", "view", "Show your group"),
                        Lang.CMD_HELP.getCommand("group", "demote [player]", "Demote a player"),
                        Lang.CMD_HELP.getCommand("group", "info [group]", "Show a group's info"),
                        Lang.CMD_HELP.getCommand("group", "promote [player]", "Promote a player"),
                        Lang.CMD_HELP.getCommand("group", "set [group]", "Set your group"),
                        Lang.CMD_HELP.getCommand("group", "view [player]", "Show a player's group"),
                        Lang.CMD_HELP.getCommand("group", "addperm [group] [perm]", "Add a permission"),
                        Lang.CMD_HELP.getCommand("group", "remperm [group] [perm]", "Remove a permission"),
                        Lang.CMD_HELP.getCommand("group", "set [player] [group]", "Set a player's group"),
                        Lang.FOOTER.getMessage()
                };
                player.sendMessage(help);
                return true;
            } else if (args.length == 1) {
                if (args[0].toLowerCase().equals("help")) {
                    String[] help = new String[] {
                            Lang.HEADER.getServerHeader(),
                            Lang.CMD_NAME.getCommandName("group"),
                            Lang.FOOTER.getMessage(),
                            Lang.CMD_HELP.getCommand("group", "help", "Show this menu"),
                            Lang.CMD_HELP.getCommand("group", "info", "Show your group info"),
                            Lang.CMD_HELP.getCommand("group", "list", "List all groups"),
                            Lang.CMD_HELP.getCommand("group", "view", "Show your group"),
                            Lang.CMD_HELP.getCommand("group", "demote [player]", "Demote a player"),
                            Lang.CMD_HELP.getCommand("group", "info [group]", "Show a group's info"),
                            Lang.CMD_HELP.getCommand("group", "promote [player]", "Promote a player"),
                            Lang.CMD_HELP.getCommand("group", "set [group]", "Set your group"),
                            Lang.CMD_HELP.getCommand("group", "view [player]", "Show a player's group"),
                            Lang.CMD_HELP.getCommand("group", "addperm [group] [perm]", "Add a permission"),
                            Lang.CMD_HELP.getCommand("group", "remperm [group] [perm]", "Remove a permission"),
                            Lang.CMD_HELP.getCommand("group", "set [player] [group]", "Set a player's group"),
                            Lang.FOOTER.getMessage()
                    };
                    player.sendMessage(help);
                    return true;
                } else if (args[0].toLowerCase().equals("info")) {
                    StringBuilder perms = new StringBuilder();
                    ChatColor color = ChatColor.GREEN;
                    for (String perm : user.getGroup().getPermissions()) {
                        String permission = perm;
                        if (perm.startsWith("-")) {
                            color = ChatColor.RED;
                            permission = permission.substring(1);
                        }
                        perms.append(color).append(permission).append(ChatColor.DARK_GRAY).append(", ");
                    }
                    String[] list = new String[] {
                            Lang.HEADER.getServerHeader(),
                            Lang.INFO.getInfo(user.getGroup().getName()),
                            Lang.FOOTER.getMessage(),
                            ChatColor.DARK_GRAY + "Name" + ChatColor.GRAY + ": " + ChatColor.translateAlternateColorCodes('&', user.getGroup().getPrefix().substring(0, 2)) + user.getGroup().getName(),
                            ChatColor.DARK_GRAY + "Prefix" + ChatColor.GRAY + ": " + ChatColor.DARK_GRAY + "[" + ChatColor.translateAlternateColorCodes('&', user.getGroup().getPrefix()) + ChatColor.DARK_GRAY + "]",
                            ChatColor.DARK_GRAY + "Priority" + ChatColor.GRAY + ": " + ChatColor.AQUA + user.getGroup().getPriority(),
                            ChatColor.DARK_GRAY + "Default" + ChatColor.GRAY + ": " + (user.getGroup().isDef() ? ChatColor.GREEN : ChatColor.RED) + user.getGroup().isDef(),
                            ChatColor.DARK_GRAY + "Inherits" + ChatColor.GRAY + ": " + ChatColor.GRAY + user.getGroup().getInherits(),
                            ChatColor.DARK_GRAY + "Permissions" + ChatColor.GRAY + ": " + perms.toString().substring(0, perms.length() -2),
                            Lang.FOOTER.getMessage()
                    };
                    player.sendMessage(list);
                } else if (args[0].toLowerCase().equals("list")) {
                    String[] list = new String[] {
                            Lang.HEADER.getServerHeader(),
                            Lang.LIST.getListName("groups"),
                            Lang.FOOTER.getMessage(),
                    };
                    player.sendMessage(list);
                    for (Group group : NEPermissions.getGroupManager().getGroups()) {
                        String line = ChatColor.DARK_GRAY + " - " + ChatColor.translateAlternateColorCodes('&', group.getPrefix().substring(0, 2)) + group.getName() + ChatColor.DARK_GRAY + "["
                                + ChatColor.AQUA + group.getPriority() + ChatColor.DARK_GRAY + "]";
                        player.sendMessage(line);
                    }
                    player.sendMessage(Lang.FOOTER.getMessage());
                    return true;
                } else if (args[0].toLowerCase().equals("view")) {
                    player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.GRAY + "Your current group is "
                            + ChatColor.translateAlternateColorCodes('&', user.getGroup().getPrefix().substring(0, 2) + user.getGroup().getName()) + ChatColor.GRAY + ".");
                    return true;
                } else {
                    player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + Lang.SYNTAX_ERROR.getMessage());
                    return true;
                }
            } else if (args.length == 2) {
                if (args[0].toLowerCase().equals("demote")) {
                    String name = args[1];
                    if (!NEPermissions.getUserRegistry().userExists(Bukkit.getOfflinePlayer(name).getUniqueId())) {
                        player.sendMessage(Lang.PLAYER_NULL.getServerChatTag() + Lang.PLAYER_NULL.getMessage());
                        return true;
                    }

                    if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(name))) {
                        Player target = Bukkit.getPlayer(name);
                        UserModel tuser = NEPermissions.getUserRegistry().getUser(target.getUniqueId());

                        if (tuser.getGroup().getPriority() == 0) {
                            player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.RED + "That player is already the lowest rank!");
                            return true;
                        }

                        tuser.setGroup(getGroupManager().getGroup(tuser.getGroup().getPriority() -1));
                        NEPermissions.getPluginManager().callEvent(new GroupChangeEvent(target));
                        player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.GRAY + "You have demoted " + ChatColor.BLUE + target.getName() + ChatColor.GRAY + " to " + ChatColor.translateAlternateColorCodes('&',
                                tuser.getGroup().getPrefix().substring(0, 2) + tuser.getGroup().getName()) + ChatColor.GRAY + ".");
                        target.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.GRAY + "You have been demoted to " + ChatColor.translateAlternateColorCodes('&',
                                tuser.getGroup().getPrefix().substring(0, 2) + tuser.getGroup().getName()) + ChatColor.GRAY + ".");
                    } else {
                        OfflinePlayer target = Bukkit.getOfflinePlayer(name);
                        UserModel tuser = NEPermissions.getUserRegistry().getUser(target.getUniqueId());

                        if (tuser.getGroup().getPriority() == 0) {
                            player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.RED + "That player is already the lowest rank!");
                            return true;
                        }

                        tuser.setGroup(getGroupManager().getGroup(tuser.getGroup().getPriority() -1));
                        player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.GRAY + "You have demoted " + ChatColor.BLUE + target.getName() + ChatColor.GRAY + " to " + ChatColor.translateAlternateColorCodes('&',
                                tuser.getGroup().getPrefix().substring(0, 2) + tuser.getGroup().getName()) + ChatColor.GRAY + ".");
                    }
                    return true;
                } else if (args[0].toLowerCase().equals("info")) {
                    String name = args[1];
                    if (!getGroupManager().exists(name)) {
                        player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.RED + "That group does not exist!");
                        return true;
                    }

                    Group group = getGroupManager().getGroup(name);
                    StringBuilder perms = new StringBuilder();
                    ChatColor color = ChatColor.GREEN;
                    for (String perm : group.getPermissions()) {
                        String permission = perm;
                        if (perm.startsWith("-")) {
                            color = ChatColor.RED;
                            permission = permission.substring(1);
                        }
                        perms.append(color).append(permission).append(ChatColor.DARK_GRAY).append(", ");
                    }
                    String[] list = new String[] {
                            Lang.HEADER.getServerHeader(),
                            Lang.INFO.getInfo(group.getName()),
                            Lang.FOOTER.getMessage(),
                            ChatColor.DARK_GRAY + "Name" + ChatColor.GRAY + ": " + ChatColor.translateAlternateColorCodes('&', group.getPrefix().substring(0, 2)) + group.getName(),
                            ChatColor.DARK_GRAY + "Prefix" + ChatColor.GRAY + ": " + ChatColor.DARK_GRAY + "[" + ChatColor.translateAlternateColorCodes('&', group.getPrefix()) + ChatColor.DARK_GRAY + "]",
                            ChatColor.DARK_GRAY + "Priority" + ChatColor.GRAY + ": " + ChatColor.AQUA + group.getPriority(),
                            ChatColor.DARK_GRAY + "Default" + ChatColor.GRAY + ": " + (group.isDef() ? ChatColor.GREEN : ChatColor.RED) + group.isDef(),
                            ChatColor.DARK_GRAY + "Inherits" + ChatColor.GRAY + ": " + ChatColor.GRAY + group.getInherits(),
                            ChatColor.DARK_GRAY + "Permissions" + ChatColor.GRAY + ": " + perms.toString().substring(0, perms.length() -2),
                            Lang.FOOTER.getMessage()
                    };
                    player.sendMessage(list);
                    return true;
                } else if (args[0].toLowerCase().equals("promote")) {
                    String name = args[1];
                    if (!NEPermissions.getUserRegistry().userExists(Bukkit.getOfflinePlayer(name).getUniqueId())) {
                        player.sendMessage(Lang.PLAYER_NULL.getServerChatTag() + Lang.PLAYER_NULL.getMessage());
                        return true;
                    }

                    if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(name))) {
                        Player target = Bukkit.getPlayer(name);
                        UserModel tuser = NEPermissions.getUserRegistry().getUser(target.getUniqueId());

                        if (tuser.getGroup().getPriority() == 7) {
                            player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.RED + "That player is already the highest rank!");
                            return true;
                        }

                        tuser.setGroup(getGroupManager().getGroup(tuser.getGroup().getPriority() +1));
                        player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.GRAY + "You have promoted " + ChatColor.BLUE + target.getName() + ChatColor.GRAY + " to " + ChatColor.translateAlternateColorCodes('&',
                                tuser.getGroup().getPrefix().substring(0, 2) + tuser.getGroup().getName()) + ChatColor.GRAY + ".");
                        NEPermissions.getPluginManager().callEvent(new GroupChangeEvent(target));
                        target.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.GRAY + "You have been promoted to " + ChatColor.translateAlternateColorCodes('&',
                                tuser.getGroup().getPrefix().substring(0, 2) + tuser.getGroup().getName()) + ChatColor.GRAY + ".");
                    } else {
                        OfflinePlayer target = Bukkit.getOfflinePlayer(name);
                        UserModel tuser = NEPermissions.getUserRegistry().getUser(target.getUniqueId());

                        if (tuser.getGroup().getPriority() == 7) {
                            player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.RED + "That player is already the highest rank!");
                            return true;
                        }


                        tuser.setGroup(getGroupManager().getGroup(tuser.getGroup().getPriority() +1));
                        player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.GRAY + "You have promoted " + ChatColor.BLUE + target.getName() + ChatColor.GRAY + " to " + ChatColor.translateAlternateColorCodes('&',
                                tuser.getGroup().getPrefix().substring(0, 2) + tuser.getGroup().getName()) + ChatColor.GRAY + ".");
                    }
                    return true;
                } else if (args[0].toLowerCase().equals("set")) {
                    String name = args[1];
                    if (!getGroupManager().exists(name)) {
                        player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.RED + "That group does not exist!");
                        return true;
                    }

                    Group group = getGroupManager().getGroup(name);
                    user.setGroup(group);
                    NEPermissions.getPluginManager().callEvent(new GroupChangeEvent(player));
                    player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.GRAY + "You have set your group to " + ChatColor.translateAlternateColorCodes(
                            '&', group.getPrefix().substring(0, 2)) + group.getName() + ChatColor.GRAY + ".");
                    return true;
                } else if (args[0].toLowerCase().equals("view")) {
                    String name = args[1];
                    if (!NEPermissions.getUserRegistry().userExists(Bukkit.getOfflinePlayer(name).getUniqueId())) {
                        player.sendMessage(Lang.PLAYER_NULL.getServerChatTag() + Lang.PLAYER_NULL.getMessage());
                        return true;
                    }


                    if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(name))) {
                        Player target = Bukkit.getPlayer(name);
                        UserModel tuser = NEPermissions.getUserRegistry().getUser(target.getUniqueId());

                        player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.BLUE + target.getName() + "'s " + ChatColor.GRAY + " group is set to "
                                + ChatColor.translateAlternateColorCodes('&', tuser.getGroup().getPrefix().substring(0, 2)) + tuser.getGroup().getName() + ChatColor.GRAY + ".");
                    } else {
                        OfflinePlayer target = Bukkit.getOfflinePlayer(name);
                        UserModel tuser = NEPermissions.getUserRegistry().getUser(target.getUniqueId());

                        player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.BLUE + target.getName() + "'s " + ChatColor.GRAY + " group is set to "
                                + ChatColor.translateAlternateColorCodes('&', tuser.getGroup().getPrefix().substring(0, 2)) + tuser.getGroup().getName() + ChatColor.GRAY + ".");
                    }
                    return true;
                } else {
                    player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + Lang.SYNTAX_ERROR.getMessage());
                    return true;                }
            } else if (args.length == 3) {
                if (args[0].toLowerCase().equals("addperm")) {
                    String name = args[1];
                    String permission = args[2];
                    if (!getGroupManager().exists(name)) {
                        player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.RED + "That group does not exist!");
                        return true;
                    }

                    Group group = NEPermissions.getGroupManager().getGroup(name);
                    if (group.getPermissions().contains(permission)) {
                        player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.RED + "That group already contains that permission.");
                        return true;
                    }

                    group.getPermissions().add(permission);
                    NEPermissions.getGroupManager().reloadPerms();
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        UserModel users = NEPermissions.getUserRegistry().getUser(players.getUniqueId());
                        if (users.getGroup().getName().equals(group.getName())) {
                            NEPermissions.getPermissionManager().addPermission(players, permission);
                        }
                    }
                    player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.GRAY + "You have added permission " + ChatColor.AQUA + permission + ChatColor.GRAY + " to group "
                            + ChatColor.translateAlternateColorCodes('&', group.getPrefix().substring(0, 2) + group.getName()) + ChatColor.GRAY + ".");
                    return true;
                } else if (args[0].toLowerCase().equals("remperm")) {
                    String name = args[1];
                    String permission = args[2];
                    if (!getGroupManager().exists(name)) {
                        player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.RED + "That group does not exist!");
                        return true;
                    }

                    Group group = NEPermissions.getGroupManager().getGroup(name);
                    if (!group.getPermissions().contains(permission)) {
                        player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.RED + "That group does not contain that permission.");
                        return true;
                    }
                    group.getPermissions().remove(permission);
                    NEPermissions.getGroupManager().reloadPerms();
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        UserModel users = NEPermissions.getUserRegistry().getUser(players.getUniqueId());
                        if (users.getGroup().getName().equals(group.getName())) {
                            NEPermissions.getPermissionManager().removePermission(players, permission);
                        }
                    }
                    player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.GRAY + "You have removed permission " + ChatColor.AQUA + permission + ChatColor.GRAY + " from group "
                            + ChatColor.translateAlternateColorCodes('&', group.getPrefix().substring(0, 2) + group.getName()) + ChatColor.GRAY + ".");
                    return true;
                } else if (args[0].toLowerCase().equals("set")) {
                    String name = args[1];
                    String gname = args[2];
                    if (!NEPermissions.getUserRegistry().userExists(Bukkit.getOfflinePlayer(name).getUniqueId())) {
                        player.sendMessage(Lang.PLAYER_NULL.getServerChatTag() + Lang.PLAYER_NULL.getMessage());
                        return true;
                    }

                    if (!getGroupManager().exists(gname)) {
                        player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.RED + "That group does not exist!");
                        return true;
                    }

                    Group group = getGroupManager().getGroup(gname);
                    if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(name))) {
                        Player target = Bukkit.getPlayer(name);
                        UserModel tuser = NEPermissions.getUserRegistry().getUser(target.getUniqueId());

                        tuser.setGroup(group);
                        NEPermissions.getPluginManager().callEvent(new GroupChangeEvent(target));
                        player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.GRAY + "You have set " + ChatColor.BLUE + target.getName() + "'s " + ChatColor.GRAY + " group to " + ChatColor.translateAlternateColorCodes('&',
                                tuser.getGroup().getPrefix().substring(0, 2) + tuser.getGroup().getName()) + ChatColor.GRAY + ".");
                        target.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.GRAY + "Your group has been set to " + ChatColor.translateAlternateColorCodes('&',
                                tuser.getGroup().getPrefix().substring(0, 2) + tuser.getGroup().getName()) + ChatColor.GRAY + ".");
                    } else {
                        OfflinePlayer target = Bukkit.getOfflinePlayer(name);
                        UserModel tuser = NEPermissions.getUserRegistry().getUser(target.getUniqueId());

                        tuser.setGroup(group);
                        player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.GRAY + "You have set " + ChatColor.BLUE + Bukkit.getOfflinePlayer(name).getName() + "'s " + ChatColor.GRAY + " group to " + ChatColor.translateAlternateColorCodes('&',
                                tuser.getGroup().getPrefix().substring(0, 2) + tuser.getGroup().getName()) + ChatColor.GRAY + ".");
                    }
                    return true;
                } else {
                    player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + Lang.SYNTAX_ERROR.getMessage());
                    return true;
                }
            }
        }
        return true;
    }
}
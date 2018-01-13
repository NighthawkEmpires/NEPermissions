package net.nighthawkempires.permissions.group;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.nighthawkempires.core.chat.tag.PlayerTag;
import net.nighthawkempires.permissions.NEPermissions;
import net.nighthawkempires.permissions.user.UserModel;
import org.bukkit.entity.Player;


public class GroupTag extends PlayerTag {

    public String getName() {
        return "group";
    }

    public TextComponent getComponentFor(Player player) {
        UserModel user = NEPermissions.getUserRegistry().getUser(player.getUniqueId());
        if (user.getDonor() != null) {
            if (user.getGroup().getName().toLowerCase().equals("banned") || user.getGroup().getName().toLowerCase().equals("guest") || user.getGroup().getName().toLowerCase().equals("member")) {
                return null;
            }
        }
        TextComponent tag = new TextComponent("[");
        tag.setColor(ChatColor.DARK_GRAY);
        TextComponent mid = new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', user.getGroup().getPrefix())));
        tag.addExtra(mid);
        tag.addExtra("]");
        tag.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', "&8&lGroup&7&l: "
                + user.getGroup().getPrefix().substring(0, 2) + "&l" + user.getGroup().getName()))));
        return tag;
    }

    public int getPriority() {
        return 5;
    }
}

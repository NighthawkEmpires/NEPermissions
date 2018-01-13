package net.nighthawkempires.permissions.status;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.nighthawkempires.core.chat.tag.PlayerTag;
import net.nighthawkempires.permissions.user.UserModel;
import org.bukkit.entity.Player;

import static net.nighthawkempires.permissions.NEPermissions.getUserRegistry;

public class StatusTag extends PlayerTag {

    public String getName() {
        return "status";
    }

    public TextComponent getComponentFor(Player player) {
        UserModel user = getUserRegistry().getUser(player.getUniqueId());
        if (user.getStatus() == null) {
            return null;
        } else {
            TextComponent tag = new TextComponent("[");
            tag.setColor(ChatColor.DARK_GRAY);
            TextComponent mid = new TextComponent(ChatColor.translateAlternateColorCodes('&', user.getStatus().getPrefix()));
            tag.addExtra(mid);
            tag.addExtra("]");
            tag.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', "&8&lStatus&7&l: "
                    + user.getStatus().getPrefix().substring(0, 2) + "&l" + user.getStatus().getName()))));
            return tag;
        }
    }

    public int getPriority() {
        return 3;
    }
}

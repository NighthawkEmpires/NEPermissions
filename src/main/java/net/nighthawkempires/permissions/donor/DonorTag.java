package net.nighthawkempires.permissions.donor;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.nighthawkempires.core.chat.tag.PlayerTag;
import net.nighthawkempires.permissions.NEPermissions;
import net.nighthawkempires.permissions.user.UserModel;
import org.bukkit.entity.Player;

public class DonorTag extends PlayerTag {

    public String getName() {
        return "donor";
    }

    public TextComponent getComponentFor(Player player) {
        UserModel user = NEPermissions.getUserRegistry().getUser(player.getUniqueId());
        if (user.getDonor() != null) {
            if (user.getGroup().getPermissions().contains("ne.staff") || user.getGroup().getPermissions().contains("ne.builder")) {
                return null;
            }

            TextComponent tag = new TextComponent("[");
            tag.setColor(ChatColor.DARK_GRAY);
            TextComponent mid = new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', user.getDonor().getPrefix())));
            tag.addExtra(mid);
            tag.addExtra("]");
            tag.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', "&8&lDonor&7&l: "
                    + user.getDonor().getPrefix().substring(0, 2) + "&l" + user.getDonor().getName()))));
            return tag;
        }
        return null;
    }

    public int getPriority() {
        return 4;
    }
}
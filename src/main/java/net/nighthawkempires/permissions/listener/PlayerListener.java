package net.nighthawkempires.permissions.listener;

import net.nighthawkempires.permissions.NEPermissions;
import net.nighthawkempires.permissions.event.GroupChangeEvent;
import net.nighthawkempires.permissions.user.UserModel;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

public class PlayerListener implements Listener {

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        if (!NEPermissions.getUserRegistry().getRegisteredData().containsKey(player.getUniqueId().toString())) {
            NEPermissions.getUserRegistry().loadFromDb(player.getUniqueId().toString());
        }
        UserModel user = NEPermissions.getUserRegistry().getUser(player.getUniqueId());

        NEPermissions.getPermissionManager().setupPermissions(player);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UserModel user = NEPermissions.getUserRegistry().getUser(player.getUniqueId());

        event.getPlayer().setPlayerListName(ChatColor.DARK_GRAY + "[" + ChatColor.translateAlternateColorCodes('&', user.getGroup().getPrefix().substring(0, 2)) + user.getGroup().getPrefix().substring(2,3)
                + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + player.getName());
    }


    @EventHandler
    public void onChange(GroupChangeEvent event) {
        Player player = event.getPlayer();
        UserModel user = NEPermissions.getUserRegistry().getUser(player.getUniqueId());

        NEPermissions.getPermissionManager().setupPermissions(player);
        event.getPlayer().setPlayerListName(ChatColor.DARK_GRAY + "[" + ChatColor.translateAlternateColorCodes('&', user.getGroup().getPrefix().substring(0, 2)) + user.getGroup().getPrefix().substring(2,3)
                + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + player.getName());
    }
}

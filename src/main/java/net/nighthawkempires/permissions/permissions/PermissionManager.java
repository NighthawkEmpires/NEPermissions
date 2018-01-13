package net.nighthawkempires.permissions.permissions;

import com.google.common.collect.Maps;
import net.nighthawkempires.permissions.NEPermissions;
import net.nighthawkempires.permissions.user.UserModel;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

public class PermissionManager {

    private ConcurrentMap<UUID, PermissionAttachment> attachmentMap;

    public PermissionManager() {
        attachmentMap = Maps.newConcurrentMap();
    }

    public ConcurrentMap<UUID, PermissionAttachment> getAttachmentMap() {
        return attachmentMap;
    }

    public void setupPermissions(Player player) {
        PermissionAttachment attachment = player.addAttachment(NEPermissions.getPlugin());
        attachment.getPermissions().clear();
        getAttachmentMap().put(player.getUniqueId(), attachment);
        UserModel user = NEPermissions.getUserRegistry().getUser(player.getUniqueId());

        for (String string : user.getGroup().getPermissions()) {
            if (string.startsWith("-")) {
                String perm = string.substring(1);
                attachment.setPermission(perm, false);
            } else {
                attachment.setPermission(string, true);
            }
        }

        if (!user.getPermissions().isEmpty()) {
            for (String string : user.getPermissions()) {
                if (string.startsWith("-")) {
                    String perm = string.substring(1);
                    attachment.setPermission(perm, false);
                } else {
                    attachment.setPermission(string, true);
                }
            }
        }
    }

    public void addPermission(Player player, String permission) {
        if (getAttachmentMap().containsKey(player.getUniqueId())) {
            if (permission.startsWith("-")) {
                getAttachmentMap().get(player.getUniqueId()).setPermission(permission.substring(1), false);
            } else {
                getAttachmentMap().get(player.getUniqueId()).setPermission(permission, true);
            }
        }
    }

    public void removePermission(Player player, String permission) {
        if (getAttachmentMap().containsKey(player.getUniqueId())) {
            getAttachmentMap().get(player.getUniqueId()).unsetPermission(permission);
        }
    }
}

package net.nighthawkempires.permissions.user;

import com.demigodsrpg.util.datasection.DataSection;
import com.demigodsrpg.util.datasection.Model;
import com.google.common.collect.ImmutableList;
import net.nighthawkempires.permissions.NEPermissions;
import net.nighthawkempires.permissions.donor.Donor;
import net.nighthawkempires.permissions.group.Group;
import net.nighthawkempires.permissions.status.Status;
import org.bukkit.Bukkit;

import java.util.*;

public class UserModel implements Model {

    private Group group;
    private Status status;
    private Donor donor;
    private List<String> permissions;
    private UUID uuid;
    private String name;

    public UserModel(UUID uuid) {
        this.uuid = uuid;
        this.name = Bukkit.getOfflinePlayer(uuid).getName();
        this.group = NEPermissions.getGroupManager().getDefault();
        this.permissions = new ArrayList<>();
    }

    public UserModel(String id, DataSection data) {
        this.uuid = UUID.fromString(id);
        this.name = Bukkit.getOfflinePlayer(uuid).getName();
        this.group = NEPermissions.getGroupManager().getGroup(data.getString("group"));
        this.permissions = data.getStringList("permissions");

        if (data.isString("status")) {
            this.status = NEPermissions.getStatusManager().getStatus(data.getString("status"));
        }

        if (data.isString("donor")) {
            this.donor = NEPermissions.getDonorManager().getDonor(data.getString("donor"));
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        NEPermissions.getUserRegistry().register(this);
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
        NEPermissions.getUserRegistry().register(this);
    }

    @Deprecated
    public ImmutableList<String> getPermissions() {
        return ImmutableList.copyOf(permissions);
    }

    public void addPermission(String permission) {
        permissions.add(permission);
        NEPermissions.getUserRegistry().register(this);
    }

    public void removePermission(String permission) {
        permissions.add(permission);
        NEPermissions.getUserRegistry().register(this);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
        NEPermissions.getUserRegistry().register(this);
    }

    public Donor getDonor() {
        return donor;
    }

    public void setDonor(Donor donor) {
        this.donor = donor;
        NEPermissions.getUserRegistry().register(this);
    }

    public UUID getUUID() {
        return uuid;
    }

    @Override
    public String getKey() {
        return uuid.toString();
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("group", group.getName().toLowerCase());
        map.put("permissions", permissions);
        if (status != null) {
            map.put("status", status.getName().toLowerCase());
        }

        if (donor != null) {
            map.put("donor", donor.getName().toLowerCase());
        }
        return map;
    }
}

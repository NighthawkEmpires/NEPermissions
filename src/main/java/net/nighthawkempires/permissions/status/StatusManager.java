package net.nighthawkempires.permissions.status;

import com.google.common.collect.Lists;
import net.nighthawkempires.core.NECore;
import net.nighthawkempires.core.file.FileType;
import net.nighthawkempires.permissions.group.Group;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.logging.Level;

public class StatusManager {

    private List<Status> statuses;

    public StatusManager() {
        statuses = Lists.newArrayList();
        loadStatuses();
    }

    public List<Status> getStatuses() {
        return statuses;
    }

    public Status getStatus(String name) {
        if (exists(name)) {
            for (Status status : getStatuses()) {
                if (status.getName().toLowerCase().equals(name.toLowerCase())) {
                    return status;
                }
            }
        }
        return null;
    }

    public boolean exists(String name) {
        for (Status status : getStatuses()) {
            if (status.getName().toLowerCase().equals(name.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public void loadStatuses() {
        getStatuses().clear();
        try {
            if (getGroupFile().isSet("statuses")) {
                for (String string : getGroupFile().getConfigurationSection("statuses").getKeys(false)) {
                    ConfigurationSection section = getGroupFile().getConfigurationSection("statuses." + string);
                    getStatuses().add(new Status(string, section.getString("prefix")));
                }
            }
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.WARNING, "[NEPermissions] Groups file empty, or could not be loaded...");
        }
    }

    public void saveStatuses() {
        getGroupFile().set("statuses", null);
        saveGroupFile();
        for (Status status : getStatuses()) {
            getGroupFile().set("statuses." + status.getName().toLowerCase() + ".prefix", status.getPrefix());
            saveGroupFile();
        }
    }

    public void reload() {
        saveStatuses();
        loadStatuses();
    }

    private FileConfiguration getGroupFile() {
        return NECore.getFileManager().get(FileType.GROUP);
    }

    private void saveGroupFile() {
        NECore.getFileManager().save(FileType.GROUP, true);
    }
}

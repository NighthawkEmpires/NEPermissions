package net.nighthawkempires.permissions.group;

import com.google.common.collect.Lists;
import net.nighthawkempires.core.NECore;
import net.nighthawkempires.core.file.FileType;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.logging.Level;

public class GroupManager {

    private List<Group> groups;

    public GroupManager() {
        groups = Lists.newArrayList();
        loadGroups();
    }

    public List<Group> getGroups() {
        return groups;
    }

    public Group getGroup(String name) {
        if (exists(name)) {
            for (Group group : getGroups()) {
                if (group.getName().toLowerCase().equals(name.toLowerCase())) {
                    return group;
                }
            }
        }
        return null;
    }

    public Group getGroup(int priority) {
        for (Group group : getGroups()) {
            if (group.getPriority() == priority) {
                return group;
            }
        }
        return null;
    }

    public Group getDefault() {
        for (Group group : getGroups()) {
            if (group.isDef()) {
                return group;
            }
        }
        return null;
    }

    public boolean exists(String name) {
        for (Group group : getGroups()) {
            if (group.getName().toLowerCase().equals(name.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public void loadGroups() {
        getGroups().clear();
        try {
            if (getGroupFile().isSet("groups")) {
                for (String string : getGroupFile().getConfigurationSection("groups").getKeys(false)) {
                    ConfigurationSection section = getGroupFile().getConfigurationSection("groups." + string);
                    getGroups().add(new Group(string, section.getString("prefix"), section.getInt("priority"),
                            (section.isSet("inherits") ? section.getString("inherits") : "banned"),
                            section.getStringList("permissions"), (section.isSet("default") && section.getBoolean("default"))));
                }
            }
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.WARNING, "[NEPermissions] Groups file empty, or could not be loaded...");
        }

        reloadPerms();
    }

    public void reloadPerms() {
        for (Group group : getGroups()) {
            for (Group inherit : getGroups()) {
                if (group.getInherits().toLowerCase().equals(inherit.getName().toLowerCase())) {
                    for (String string : inherit.getPermissions()) {
                        if (!group.getPermissions().contains(string)) {
                            group.getPermissions().add(string);
                        }
                    }
                }
            }
        }

        for (Group group : getGroups()) {
            for (Group inherit : getGroups()) {
                if (group.getInherits().toLowerCase().equals(inherit.getName().toLowerCase())) {
                    for (String string : inherit.getPermissions()) {
                        if (!group.getPermissions().contains(string)) {
                            group.getPermissions().add(string);
                        }
                    }
                }
            }
        }
    }

    public void saveGroups() {
        getGroupFile().set("groups", null);
        saveGroupFile();
        for (Group group : getGroups()) {
            getGroupFile().set("groups." + group.getName().toLowerCase() + ".prefix", group.getPrefix());
            getGroupFile().set("groups." + group.getName().toLowerCase() + ".priority", group.getPriority());
            getGroupFile().set("groups." + group.getName().toLowerCase() + ".inherits", group.getInherits());
            getGroupFile().set("groups." + group.getName().toLowerCase() + ".permissions", group.getPermissions());
            saveGroupFile();
        }
    }

    private FileConfiguration getGroupFile() {
        return NECore.getFileManager().get(FileType.GROUP);
    }

    private void saveGroupFile() {
        NECore.getFileManager().save(FileType.GROUP, true);
    }
}

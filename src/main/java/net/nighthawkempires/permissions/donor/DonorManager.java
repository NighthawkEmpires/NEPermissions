package net.nighthawkempires.permissions.donor;

import com.google.common.collect.Lists;
import net.nighthawkempires.core.NECore;
import net.nighthawkempires.core.file.FileType;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.logging.Level;

public class DonorManager {

    private List<Donor> donors;

    public DonorManager() {
        donors = Lists.newArrayList();
        loadDonor();
    }

    public List<Donor> getDonors() {
        return donors;
    }

    public Donor getDonor(String name) {
        if (exists(name)) {
            for (Donor donor : getDonors()) {
                if (donor.getName().toLowerCase().equals(name.toLowerCase())) {
                    return donor;
                }
            }
        }
        return null;
    }

    public boolean exists(String name) {
        for (Donor donor : getDonors()) {
            if (donor.getName().toLowerCase().equals(name.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public void loadDonor() {
        getDonors().clear();
        try {
            if (getGroupFile().isSet("donor")) {
                for (String string : getGroupFile().getConfigurationSection("donor").getKeys(false)) {
                    ConfigurationSection section = getGroupFile().getConfigurationSection("donor." + string);
                    getDonors().add(new Donor(string, section.getString("prefix"), section.getStringList("permissions")));
                }
            }
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.WARNING, "[NEPermissions] Groups file empty, or could not be loaded...");
        }
    }

    public void saveDonor() {
        getGroupFile().set("donor", null);
        saveGroupFile();
        for (Donor donor : getDonors()) {
            getGroupFile().set("donor." + donor.getName().toLowerCase() + ".prefix", donor.getPrefix());
            getGroupFile().set("donor." + donor.getName().toLowerCase() + ".permissions", donor.getPermissions());
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

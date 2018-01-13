package net.nighthawkempires.permissions.donor;

import java.util.List;

public class Donor {

    private String name;
    private String prefix;
    private List<String> permissions;

    public Donor(String name, String prefix, List<String> permissions) {
        this.name = name.substring(0, 1).toUpperCase() + name.substring(1);
        this.prefix = prefix;
        this.permissions = permissions;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public List<String> getPermissions() {
        return permissions;
    }
}
package net.nighthawkempires.permissions.group;

import java.util.List;

public class Group {

    private String name;
    private String prefix;
    private int priority;
    private String inherits;
    private List<String> permissions;
    private boolean def;

    public Group(String name, String prefix, int priority, String inherits, List<String> permissions, boolean def) {
        this.name = name.substring(0, 1).toUpperCase() + name.substring(1);
        this.prefix = prefix;
        this.priority = priority;
        this.inherits = inherits;
        this.permissions = permissions;
        this.def = def;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getPriority() {
        return priority;
    }

    public String getInherits() {
        return inherits;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public boolean isDef() {
        return def;
    }
}

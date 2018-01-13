package net.nighthawkempires.permissions.status;

public class Status {

    private String name;
    private String prefix;

    public Status(String name, String prefix) {
        this.name = name.substring(0, 1).toUpperCase() + name.substring(1);
        this.prefix = prefix;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }
}

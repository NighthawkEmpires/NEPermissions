package net.nighthawkempires.permissions;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import net.nighthawkempires.core.NECore;
import net.nighthawkempires.core.file.FileFolder;
import net.nighthawkempires.permissions.command.*;
import net.nighthawkempires.permissions.donor.DonorManager;
import net.nighthawkempires.permissions.donor.DonorTag;
import net.nighthawkempires.permissions.group.GroupManager;
import net.nighthawkempires.permissions.group.GroupTag;
import net.nighthawkempires.permissions.listener.PlayerListener;
import net.nighthawkempires.permissions.permissions.PermissionManager;
import net.nighthawkempires.permissions.scoreboard.GroupScoreboards;
import net.nighthawkempires.permissions.status.StatusManager;
import net.nighthawkempires.permissions.status.StatusTag;
import net.nighthawkempires.permissions.user.registry.FUserRegistry;
import net.nighthawkempires.permissions.user.registry.MUserRegistry;
import net.nighthawkempires.permissions.user.registry.UserRegistry;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class NEPermissions extends JavaPlugin {

    private static NEPermissions instance;
    private static Plugin plugin;

    private static DonorManager donorManager;
    private static GroupManager groupManager;
    private static PermissionManager permissionManager;
    private static StatusManager statusManager;

    private static PluginManager pluginManager;

    private static MongoDatabase mongoDatabase;

    private static UserRegistry userRegistry;

    public void onEnable() {
        instance = this;
        plugin = this;
        donorManager = new DonorManager();
        groupManager = new GroupManager();
        permissionManager = new PermissionManager();
        statusManager = new StatusManager();
        pluginManager = Bukkit.getPluginManager();

        if (NECore.getSettings().mongoEnabledGuilds) {
            try {
                String hostname = NECore.getSettings().mongoHostnameGuilds;
                String username = NECore.getSettings().mongoUsernameGuilds;
                String password = NECore.getSettings().mongoPasswordGuilds;
                ServerAddress address = new ServerAddress(hostname, 27017);
                MongoCredential credential =
                        MongoCredential.createCredential("permissions", "ne_permissions", password.toCharArray());
                mongoDatabase =
                        new MongoClient(address, credential, new MongoClientOptions.Builder().build())
                                .getDatabase("ne_permissions");
                userRegistry = new MUserRegistry(mongoDatabase);
                NECore.getLoggers().info(this, "MongoDB enabled.");
            } catch (Exception oops) {
                oops.printStackTrace();
                NECore.getLoggers().warn("MongoDB connection failed. Disabling plugin.");
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }
        } else {
            userRegistry = new FUserRegistry(FileFolder.PLAYER_PATH.getPath());
        }

        NECore.getChatFormat().add(new GroupTag()).add(new StatusTag()).add(new DonorTag());
        NECore.getScoreboardManager().addScoreboard(new GroupScoreboards());

        registerCommands();
        registerListeners();

        getUserRegistry().loadAllFromDb();
    }

    public void registerCommands() {
        this.getCommand("demote").setExecutor(new DemoteCommand());
        this.getCommand("donor").setExecutor(new DonorCommand());
        this.getCommand("group").setExecutor(new GroupCommand());
        this.getCommand("permissions").setExecutor(new PermissionsCommand());
        this.getCommand("promote").setExecutor(new PromoteCommand());
        this.getCommand("status").setExecutor(new StatusCommand());
    }

    public void registerListeners() {
        getPluginManager().registerEvents(new PlayerListener(), this);
    }

    public static NEPermissions getInstance() {
        return instance;
    }

    public static Plugin getPlugin() {
        return plugin;
    }

    public static DonorManager getDonorManager() {
        return donorManager;
    }

    public static GroupManager getGroupManager() {
        return groupManager;
    }

    public static PermissionManager getPermissionManager() {
        return permissionManager;
    }

    public static StatusManager getStatusManager() {
        return statusManager;
    }

    public static PluginManager getPluginManager() {
        return pluginManager;
    }

    public static UserRegistry getUserRegistry() {
        return userRegistry;
    }
}

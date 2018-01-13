package net.nighthawkempires.permissions.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GroupChangeEvent extends Event {

    private static HandlerList handlers = new HandlerList();
    private Player player;

    public GroupChangeEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

package de.ichwurdegeboren.coinmanager.api.events;

import de.ichwurdegeboren.coinmanager.api.ChangeCause;
import de.ichwurdegeboren.coinmanager.api.ICoinsUser;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BukkitCoinsUserUpdateEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    private final ICoinsUser coinsUser;

    private double newAmount;
    private double oldAmount;
    private ChangeCause changeCause;

    private boolean cancelled = false;

    public ICoinsUser getCoinsUser() {
        return this.coinsUser;
    }

    public BukkitCoinsUserUpdateEvent(ICoinsUser coinsUser, double newAmount, double oldAmount, ChangeCause changeCause) {
        this.coinsUser = coinsUser;
        this.newAmount = newAmount;
        this.oldAmount = oldAmount;
        this.changeCause = changeCause;
    }

    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}

package de.ichwurdegeboren.coinmanager.api.events;

import de.ichwurdegeboren.coinmanager.api.ChangeCause;
import de.ichwurdegeboren.coinmanager.api.ICoinsUser;
import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BukkitCoinsUserUpdateEvent extends Event {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    private final ICoinsUser coinsUser;

    @Getter
    private double newAmount;
    @Getter
    private double oldAmount;
    @Getter
    private ChangeCause changeCause;

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
}

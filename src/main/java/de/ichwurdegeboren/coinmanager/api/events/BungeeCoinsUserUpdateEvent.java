package de.ichwurdegeboren.coinmanager.api.events;

import de.ichwurdegeboren.coinmanager.api.ChangeCause;
import de.ichwurdegeboren.coinmanager.api.ICoinsUser;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;

public class BungeeCoinsUserUpdateEvent extends Event implements Cancellable {

    private final ICoinsUser coinsUser;
    private boolean cancelled = false;

    private double newAmount;
    private double oldAmount;
    private ChangeCause changeCause;

    public BungeeCoinsUserUpdateEvent(ICoinsUser coinsUser, double newAmount, double oldAmount, ChangeCause changeCause) {
        this.coinsUser = coinsUser;
        this.newAmount = newAmount;
        this.oldAmount = oldAmount;
        this.changeCause = changeCause;
    }

    public ICoinsUser getCoinsUser() {
        return this.coinsUser;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}

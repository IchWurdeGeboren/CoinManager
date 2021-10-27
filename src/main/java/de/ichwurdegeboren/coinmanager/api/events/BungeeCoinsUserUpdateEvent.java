package de.ichwurdegeboren.coinmanager.api.events;

import de.ichwurdegeboren.coinmanager.api.ChangeCause;
import de.ichwurdegeboren.coinmanager.api.ICoinsUser;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;

public class BungeeCoinsUserUpdateEvent extends Event implements Cancellable {

    private final ICoinsUser coinsUser;

    @Getter
    private double newAmount;
    @Getter
    private double oldAmount;
    @Getter
    private ChangeCause changeCause;

    @Getter
    @Setter
    private boolean cancelled = false;

    public BungeeCoinsUserUpdateEvent(ICoinsUser coinsUser, double newAmount, double oldAmount, ChangeCause changeCause) {
        this.coinsUser = coinsUser;
        this.newAmount = newAmount;
        this.oldAmount = oldAmount;
        this.changeCause = changeCause;
    }

    public ICoinsUser getCoinsUser() {
        return this.coinsUser;
    }

}

package de.ichwurdegeboren.coinmanager.utils;

import de.ichwurdegeboren.coinmanager.api.ICoinsUser;
import de.ichwurdegeboren.coinmanager.api.ChangeCause;
import de.ichwurdegeboren.coinmanager.main.Main;
import org.bukkit.Bukkit;

import java.util.UUID;

public class CoinUser implements ICoinsUser {

    private final Main main;

    private UUID uuid;
    private double coins;

    public CoinUser(Main main, UUID uuid, long coins) {
        this.main = main;
        this.uuid = uuid;
        this.coins = coins;
    }

    @Override
    public double getCoins() {
        return coins;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public boolean hasEnough(double amount) {
        return coins >= amount;
    }

    @Override
    public void addCoins(double coins) {
        main.callCoinsUpdateEvent(this, this.coins + coins, coins, ChangeCause.ADD);
        this.coins += coins;
    }

    @Override
    public void removeCoins(double coins) {
        main.callCoinsUpdateEvent(this, this.coins - coins, coins, ChangeCause.REMOVE);
        this.coins -= coins;
    }

    @Override
    public void setCoins(double coins) {
        main.callCoinsUpdateEvent(this, coins, this.coins, ChangeCause.SET);
        this.coins = coins;
    }
}

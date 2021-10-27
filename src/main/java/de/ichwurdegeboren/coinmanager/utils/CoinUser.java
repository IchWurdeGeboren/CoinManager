package de.ichwurdegeboren.coinmanager.utils;

import de.ichwurdegeboren.coinmanager.api.ICoinsUser;
import de.ichwurdegeboren.coinmanager.api.ChangeCause;
import de.ichwurdegeboren.coinmanager.api.events.BukkitCoinsUserUpdateEvent;
import de.ichwurdegeboren.coinmanager.main.Main;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class CoinUser implements ICoinsUser {

    private final Main main;

    private UUID uuid;
    private BigDecimal coins;

    public CoinUser(Main main, UUID uuid, double coins) {
        this.main = main;
        this.uuid = uuid;
        this.coins = new BigDecimal(Double.valueOf(coins / 100));
    }

    @Override
    public double getCoins() {
        return coins.doubleValue();
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public boolean hasEnough(double amount) {
        return coins.doubleValue() >= amount;
    }

    @SneakyThrows
    @Override
    public CompletableFuture<Boolean> addCoins(double coins) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Future<Boolean> result = main.callCoinsUpdateEvent(this, new BigDecimal(this.coins.doubleValue()).add(new BigDecimal(coins)).doubleValue(), coins, ChangeCause.ADD);
                if (result.get()) return false;
                this.coins = this.coins.add(new BigDecimal(coins));
                return true;
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            return false;
        });
    }

    @SneakyThrows
    @Override
    public CompletableFuture<Boolean> removeCoins(double coins) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Future<Boolean> result = main.callCoinsUpdateEvent(this, new BigDecimal(this.coins.doubleValue()).subtract(new BigDecimal(coins)).doubleValue(), coins, ChangeCause.REMOVE);
                if (result.get()) return false;
                this.coins = this.coins.subtract(new BigDecimal(coins));
                return true;
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            return false;
        });
    }

    @Override
    public CompletableFuture<Boolean> setCoins(double coins) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Future<Boolean> result = main.callCoinsUpdateEvent(this, coins, this.coins.doubleValue(), ChangeCause.SET);
                if (result.get()) return false;
                this.coins = new BigDecimal(coins);
                return true;
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            return false;
        });
    }
}

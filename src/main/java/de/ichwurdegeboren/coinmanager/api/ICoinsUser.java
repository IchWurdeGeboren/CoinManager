package de.ichwurdegeboren.coinmanager.api;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface ICoinsUser {

    UUID getUUID();
    double getCoins();
    CompletableFuture<Boolean> addCoins(double amount);
    CompletableFuture<Boolean> setCoins(double amount);
    CompletableFuture<Boolean> removeCoins(double amount);
    boolean hasEnough(double amount);
}

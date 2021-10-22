package de.ichwurdegeboren.coinmanager.api;

import java.util.UUID;

public interface ICoinsUser {

    UUID getUUID();
    double getCoins();
    void addCoins(double amount);
    void setCoins(double amount);
    void removeCoins(double amount);
    boolean hasEnough(double amount);
}

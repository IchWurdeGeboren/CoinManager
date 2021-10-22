package de.ichwurdegeboren.coinmanager.api;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface ICoinsManager {

    CompletableFuture<ICoinsUser> registerCoinsUser(UUID uuid);

    CompletableFuture<ICoinsUser> getCoinsUser(UUID uuid);

    void saveCoinsUser(ICoinsUser iCoinsUser);

}

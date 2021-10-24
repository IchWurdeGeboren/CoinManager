package de.ichwurdegeboren.coinmanager.main;

import de.ichwurdegeboren.coinmanager.api.ICoinsUser;
import de.ichwurdegeboren.coinmanager.api.ChangeCause;
import de.ichwurdegeboren.coinmanager.manager.CoinUserManager;
import de.ichwurdegeboren.coinmanager.utils.ConfigObject;
import de.ichwurdegeboren.coinmanager.utils.MySQL;

import java.util.logging.Logger;

public interface Main {

    String getPrefix();

    Logger getLogger();

    MySQL getMySQL();

    CoinUserManager getCoinUserManager();

    ConfigObject getConfigObject();

    void callCoinsUpdateEvent(ICoinsUser iCoinsUser, double newAmount, double oldAmount, ChangeCause changeCause);
}

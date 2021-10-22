package de.ichwurdegeboren.coinmanager.api;

public class CoinsManagerProvider {

    private static ICoinsManager iCoinsManager = null;

    public static void set(ICoinsManager iCoinsManager) {
        CoinsManagerProvider.iCoinsManager = iCoinsManager;
    }

    public static ICoinsManager get() {
        return iCoinsManager;
    }
}

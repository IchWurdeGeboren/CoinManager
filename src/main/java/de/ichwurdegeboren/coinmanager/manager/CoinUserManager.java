package de.ichwurdegeboren.coinmanager.manager;

import de.ichwurdegeboren.coinmanager.api.ICoinsManager;
import de.ichwurdegeboren.coinmanager.api.ICoinsUser;
import de.ichwurdegeboren.coinmanager.main.Main;
import de.ichwurdegeboren.coinmanager.utils.CoinUser;
import lombok.Getter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CoinUserManager implements ICoinsManager {

    private final Main main;

    public CoinUserManager(Main main) {
        this.main = main;
        schedule();
    }

    @Override
    public CompletableFuture<ICoinsUser> getCoinsUser(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            ResultSet rs = main.getMySQL().query("SELECT * FROM " + main.getConfigObject().getTablePrefix() + "coins WHERE UUID = '" + uuid.toString() + "'");
            try {
                if (rs.next())
                    return new CoinUser(main, uuid, rs.getLong("COINS"));
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return new CoinUser(main, uuid, 0);
        });
    }

    @Override
    public void saveCoinsUser(ICoinsUser iCoinsUser) {
        CompletableFuture.runAsync(() -> {
            try {
                PreparedStatement ps = main.getMySQL().getCon().prepareStatement("INSERT INTO " + main.getConfigObject().getTablePrefix() + "coins VALUES (?, ?) ON DUPLICATE KEY UPDATE COINS=?;");
                ps.setString(1, iCoinsUser.getUUID().toString());
                ps.setDouble(2, iCoinsUser.getCoins());
                ps.setDouble(3, iCoinsUser.getCoins());
                ps.execute();
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public CompletableFuture<ICoinsUser> registerCoinsUser(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                PreparedStatement ps = main.getMySQL().getCon().prepareStatement("SELECT * FROM " + main.getConfigObject().getTablePrefix() + "coins WHERE UUID=?;");
                ps.setString(1, uuid.toString());
                ResultSet rs = ps.executeQuery();
                if (rs.next())
                    return new CoinUser(main, uuid, rs.getLong("COINS"));
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                PreparedStatement ps = main.getMySQL().getCon().prepareStatement("INSERT INTO " + main.getConfigObject().getTablePrefix() + "coins VALUES (?, ?) ON DUPLICATE KEY UPDATE COINS=COINS;");
                ps.setString(1, uuid.toString());
                ps.setDouble(2, 0);
                ps.execute();
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return new CoinUser(main, uuid, 0);
        });
    }

    private void schedule() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                main.getMySQL().update("SELECT * FROM " + main.getConfigObject().getTablePrefix() + "coins WHERE UUID='asdawd'");
            }
        }, 0, 10 * 60 * 1000);
    }
}

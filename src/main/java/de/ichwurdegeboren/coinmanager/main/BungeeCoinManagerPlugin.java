package de.ichwurdegeboren.coinmanager.main;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.ichwurdegeboren.coinmanager.api.ChangeCause;
import de.ichwurdegeboren.coinmanager.api.CoinsManagerProvider;
import de.ichwurdegeboren.coinmanager.api.ICoinsUser;
import de.ichwurdegeboren.coinmanager.api.events.BungeeCoinsUserUpdateEvent;
import de.ichwurdegeboren.coinmanager.commands.bungee.*;
import de.ichwurdegeboren.coinmanager.listener.bungee.PostLoginListener;
import de.ichwurdegeboren.coinmanager.manager.CoinUserManager;
import de.ichwurdegeboren.coinmanager.utils.ConfigObject;
import de.ichwurdegeboren.coinmanager.utils.MySQL;
import lombok.Getter;
import lombok.SneakyThrows;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Getter
public class BungeeCoinManagerPlugin extends Plugin implements Main {

    private String prefix;

    private MySQL mySQL;
    private CoinUserManager coinUserManager;
    private ConfigObject configObject;

    public void onEnable() {
        loadConfig();
        loadMySQL();

        getProxy().getPluginManager().registerListener(this, new PostLoginListener(this));

        if (!configObject.isBungeeCommands()) return;
        getProxy().getPluginManager().registerCommand(this, new MoneyCommand(this));
        getProxy().getPluginManager().registerCommand(this, new AddMoneyCommand(this));
        getProxy().getPluginManager().registerCommand(this, new PayCommand(this));
        getProxy().getPluginManager().registerCommand(this, new RemoveMoneyCommand(this));
        getProxy().getPluginManager().registerCommand(this, new SetMoneyCommand(this));
    }

    @SneakyThrows
    private void loadConfig() {
        File file = new File(getDataFolder(), "config.json");
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        if (!file.exists()) {
            this.configObject = ConfigObject.builder().prefix("&6&lMoneySystem &8» &7").hostname("localhost").port(3306).database("database").username("username").password("password").tablePrefix("").bungeeCommands(true).spigotCommands(false).build();
            Files.createParentDirs(file);
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            writer.write(gson.toJson(configObject));
            writer.flush();
            writer.close();
        } else
            this.configObject = gson.fromJson(new InputStreamReader(new FileInputStream(file), "UTF-8"), ConfigObject.class);
        this.prefix = ChatColor.translateAlternateColorCodes('&', configObject.getPrefix());
    }

    private void loadMySQL() {
        CompletableFuture.runAsync(() -> {
            this.mySQL = new MySQL(
                    configObject.getHostname(),
                    String.valueOf(configObject.getPort()),
                    configObject.getDatabase(),
                    configObject.getUsername(),
                    configObject.getPassword(),
                    this);
            this.mySQL.update("CREATE TABLE IF NOT EXISTS " + configObject.getTablePrefix() + "coins(UUID VARCHAR(64), COINS BIGINT, PRIMARY KEY(UUID))");
            this.coinUserManager = new CoinUserManager(this);
            CoinsManagerProvider.set(coinUserManager);
        });
    }

    @Override
    public Future<Boolean> callCoinsUpdateEvent(ICoinsUser iCoinsUser, double newAmount, double oldAmount, ChangeCause changeCause) {
        BungeeCoinsUserUpdateEvent event = new BungeeCoinsUserUpdateEvent(iCoinsUser, newAmount, oldAmount, changeCause);
        getProxy().getPluginManager().callEvent(event);
        return Executors.newSingleThreadExecutor().submit(() -> event.isCancelled());
    }
}

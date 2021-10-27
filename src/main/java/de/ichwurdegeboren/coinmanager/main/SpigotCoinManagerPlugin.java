package de.ichwurdegeboren.coinmanager.main;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.ichwurdegeboren.coinmanager.api.CoinsManagerProvider;
import de.ichwurdegeboren.coinmanager.api.ICoinsUser;
import de.ichwurdegeboren.coinmanager.api.events.BukkitCoinsUserUpdateEvent;
import de.ichwurdegeboren.coinmanager.api.ChangeCause;
import de.ichwurdegeboren.coinmanager.commands.spigot.*;
import de.ichwurdegeboren.coinmanager.listener.spigot.JoinListener;
import de.ichwurdegeboren.coinmanager.manager.CoinUserManager;
import de.ichwurdegeboren.coinmanager.utils.ConfigObject;
import de.ichwurdegeboren.coinmanager.utils.MySQL;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Getter
public class SpigotCoinManagerPlugin extends JavaPlugin implements Main {

    private String prefix;

    private MySQL mySQL;
    private CoinUserManager coinUserManager;
    private ConfigObject configObject;

    public void onEnable() {
        loadConfig();
        loadMySQL();

        getServer().getPluginManager().registerEvents(new JoinListener(this), this);

        if (!configObject.isSpigotCommands()) return;
        getCommand("addmoney").setExecutor(new AddMoneyCommand(this));
        getCommand("setmoney").setExecutor(new SetMoneyCommand(this));
        getCommand("removemoney").setExecutor(new RemoveMoneyCommand(this));
        getCommand("money").setExecutor(new MoneyCommand(this));
        getCommand("pay").setExecutor(new PayCommand(this));
    }

    @SneakyThrows
    private void loadConfig() {
        File file = new File(getDataFolder(), "config.json");
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        if (!file.exists()) {
            this.configObject = ConfigObject.builder().prefix("&6&lMoneySystem &8» &7").hostname("localhost").port(3306).database("database").username("username").password("password").tablePrefix("").bungeeCommands(false).spigotCommands(true).build();
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
        BukkitCoinsUserUpdateEvent event = new BukkitCoinsUserUpdateEvent(iCoinsUser, newAmount, oldAmount, changeCause);
        return getServer().getScheduler().callSyncMethod(this, () -> {
            getServer().getPluginManager().callEvent(event);
            return event.isCancelled();
        });
    }
}

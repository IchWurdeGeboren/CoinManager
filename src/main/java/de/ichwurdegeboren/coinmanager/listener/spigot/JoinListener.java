package de.ichwurdegeboren.coinmanager.listener.spigot;

import de.ichwurdegeboren.coinmanager.main.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private final Main main;

    public JoinListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        main.getCoinUserManager().registerCoinsUser(event.getPlayer().getUniqueId());
    }
}

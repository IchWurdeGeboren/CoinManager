package de.ichwurdegeboren.coinmanager.listener.bungee;

import de.ichwurdegeboren.coinmanager.main.Main;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PostLoginListener implements Listener {

    private final Main main;

    public PostLoginListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        main.getCoinUserManager().registerCoinsUser(event.getPlayer().getUniqueId());
    }

}

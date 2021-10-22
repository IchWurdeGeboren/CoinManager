package de.ichwurdegeboren.coinmanager.commands.bungee;

import de.ichwurdegeboren.coinmanager.main.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class MoneyCommand extends Command {

    private final Main main;

    public MoneyCommand(Main main) {
        super("money", "", "balance", "bal", "coins");
        this.main = main;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(TextComponent.fromLegacyText(main.getPrefix() + "Du musst ein Spieler sein, um diesen Befehl nutzen zu können."));
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;

        if (args.length == 0) {
            main.getCoinUserManager().getCoinsUser(player.getUniqueId()).thenAccept(coinUser -> player.sendMessage(TextComponent.fromLegacyText(main.getPrefix() + "Du hast aktuell §b" + coinUser.getCoins() + "€§7.")));
            return;
        }
        if (args.length == 1) {
            if (player.hasPermission("moneysystem.money.other")) {
                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
                if (target == null) {
                    player.sendMessage(TextComponent.fromLegacyText(main.getPrefix() + "Der Spieler §b" + args[0] + " §7ist nicht online."));
                    return;
                }
                main.getCoinUserManager().getCoinsUser(target.getUniqueId()).thenAccept(coinUser -> player.sendMessage(TextComponent.fromLegacyText(main.getPrefix() + "§b" + target.getName() + " §7hat aktuell §b" + coinUser.getCoins() + "€§7.")));
            } else
                player.sendMessage(TextComponent.fromLegacyText(main.getPrefix() + "Dazu hast du keine Rechte."));
        } else
            player.sendMessage(TextComponent.fromLegacyText(main.getPrefix() + "Bitte benutze: /money" + (player.hasPermission("moneysystem.money.other") ? " <Spieler>" : "")));
    }
}

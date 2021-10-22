package de.ichwurdegeboren.coinmanager.commands.bungee;

import de.ichwurdegeboren.coinmanager.main.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class AddMoneyCommand extends Command {

    private final Main main;

    public AddMoneyCommand(Main main) {
        super("addmoney", "moneysystem.addmoney");
        this.main = main;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(TextComponent.fromLegacyText(main.getPrefix() + "Du musst ein Spieler sein, um diesen Befehl nutzen zu können."));
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;

        if (player.hasPermission("moneysystem.addmoney")) {
            if (args.length == 2) {
                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
                if (target == null) {
                    player.sendMessage(TextComponent.fromLegacyText(main.getPrefix() + "Der Spieler §b" + args[0] + " §7ist nicht online."));
                    return;
                }

                double amount = -1;
                try {
                    amount = Double.parseDouble(args[1]);
                } catch (NumberFormatException e) {
                    player.sendMessage(TextComponent.fromLegacyText(main.getPrefix() + "Bitte benutze: /addmoney <Spieler> <Betrag>"));
                    return;
                }
                if (amount <= 0) {
                    player.sendMessage(TextComponent.fromLegacyText(main.getPrefix() + "Bitte gib nur positive Geldbeträge an."));
                    return;
                }
                final double finalAmount = amount;

                main.getCoinUserManager().getCoinsUser(target.getUniqueId()).thenAccept(coinUser -> {
                    coinUser.addCoins(finalAmount);
                    main.getCoinUserManager().saveCoinsUser(coinUser);
                    target.sendMessage(TextComponent.fromLegacyText(main.getPrefix() + "Dir wurden §b" + finalAmount + "€ §7hinzugefügt."));
                    player.sendMessage(TextComponent.fromLegacyText(main.getPrefix() + "Du hast §b" + target.getName() + " " + finalAmount + "€ §7hinzugefügt."));
                });
            } else
                player.sendMessage(TextComponent.fromLegacyText(main.getPrefix() + "Bitte benutze: /addmoney <Spieler> <Betrag>"));
        } else
            player.sendMessage(TextComponent.fromLegacyText(main.getPrefix() + "Dazu hast du keine Rechte."));
    }
}
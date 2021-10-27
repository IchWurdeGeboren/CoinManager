package de.ichwurdegeboren.coinmanager.commands.bungee;

import de.ichwurdegeboren.coinmanager.main.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class PayCommand extends Command {

    private final Main main;

    public PayCommand(Main main) {
        super("pay");
        this.main = main;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(TextComponent.fromLegacyText(main.getPrefix() + "Du musst ein Spieler sein, um diesen Befehl nutzen zu können."));
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        if (args.length == 2) {
            double amount = -1;
            try {
                amount = Double.parseDouble(args[1]);
            } catch (NumberFormatException e) {
                player.sendMessage(TextComponent.fromLegacyText(main.getPrefix() + "Bitte benutze: /pay <Spieler> <Betrag>"));
                return;
            }
            if (amount <= 0) {
                player.sendMessage(TextComponent.fromLegacyText(main.getPrefix() + "Bitte gib nur positive Geldbeträge an."));
                return;
            }
            if(amount < 0.01) {
                player.sendMessage(TextComponent.fromLegacyText(main.getPrefix() + "Der angegebene Geldbetrag ist zu klein."));
                return;
            }
            final double finalAmount = amount;

            if (args[1].equalsIgnoreCase("*")) {
                main.getCoinUserManager().getCoinsUser(player.getUniqueId()).thenAccept(coinsUser -> {
                    if (coinsUser.getCoins() < (finalAmount * ProxyServer.getInstance().getPlayers().size())) {
                        player.sendMessage(TextComponent.fromLegacyText(main.getPrefix() + "Du hast nicht genügend Geld."));
                        return;
                    }
                    coinsUser.removeCoins(finalAmount * ProxyServer.getInstance().getPlayers().size());
                    for (ProxiedPlayer target : ProxyServer.getInstance().getPlayers())
                        if (target != player)
                            main.getCoinUserManager().getCoinsUser(target.getUniqueId()).thenAccept(targetCoinsUser -> {
                                targetCoinsUser.addCoins(finalAmount).thenAccept(bool -> {
                                    if(!bool) return;
                                    main.getCoinUserManager().saveCoinsUser(targetCoinsUser);
                                    player.sendMessage(TextComponent.fromLegacyText(main.getPrefix() + "Du hast §b" + target.getName() + " " + finalAmount + "€ §7gegeben."));
                                    target.sendMessage(TextComponent.fromLegacyText(main.getPrefix() + "Du hast von §b" + player.getName() + " " + finalAmount + "€ §7erhalten."));
                                });
                            });
                    main.getCoinUserManager().saveCoinsUser(coinsUser);
                });
                return;
            }
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(TextComponent.fromLegacyText(main.getPrefix() + "Der Spieler §b" + args[0] + " §7ist nicht online."));
                return;
            }
            if (target == player) {
                player.sendMessage(TextComponent.fromLegacyText(main.getPrefix() + "Du kannst dir selbst kein Geld geben."));
                return;
            }
            main.getCoinUserManager().getCoinsUser(player.getUniqueId()).thenAccept(coinsUser -> {
                if (coinsUser.getCoins() < finalAmount) {
                    player.sendMessage(TextComponent.fromLegacyText(main.getPrefix() + "Du hast nicht genügend Geld."));
                    return;
                }
                main.getCoinUserManager().getCoinsUser(target.getUniqueId()).thenAccept(targetCoinsUser -> {
                    coinsUser.removeCoins(finalAmount);
                    targetCoinsUser.addCoins(finalAmount);
                    player.sendMessage(TextComponent.fromLegacyText(main.getPrefix() + "Du hast §b" + target.getName() + " " + finalAmount + "€ §7gegeben."));
                    target.sendMessage(TextComponent.fromLegacyText(main.getPrefix() + "Du hast von §b" + player.getName() + " " + finalAmount + "€ §7erhalten."));
                    main.getCoinUserManager().saveCoinsUser(coinsUser);
                    main.getCoinUserManager().saveCoinsUser(targetCoinsUser);
                });
            });
        } else
            player.sendMessage(TextComponent.fromLegacyText(main.getPrefix() + "Bitte benutze: /pay <Spieler> <Betrag>"));
    }
}

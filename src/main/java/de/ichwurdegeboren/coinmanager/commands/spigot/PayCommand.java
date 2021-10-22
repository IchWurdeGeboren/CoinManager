package de.ichwurdegeboren.coinmanager.commands.spigot;

import de.ichwurdegeboren.coinmanager.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PayCommand implements CommandExecutor {

    private final Main main;

    public PayCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(main.getPrefix() + "Du musst ein Spieler sein, um diesen Befehl nutzen zu können.");
            return false;
        }

        Player player = (Player) sender;
        if (args.length == 2) {
            double amount = -1;
            try {
                amount = Double.parseDouble(args[1]);
            } catch (NumberFormatException e) {
                player.sendMessage(main.getPrefix() + "Bitte benutze: /pay <Spieler> <Betrag>");
                return false;
            }
            if (amount <= 0) {
                player.sendMessage(main.getPrefix() + "Bitte gib nur positive Geldbeträge an.");
                return false;
            }
            final double finalAmount = amount;

            if (args[1].equalsIgnoreCase("*")) {
                main.getCoinUserManager().getCoinsUser(player.getUniqueId()).thenAccept(coinsUser -> {
                    if (coinsUser.getCoins() < (finalAmount * Bukkit.getOnlinePlayers().size())) {
                        player.sendMessage(main.getPrefix() + "Du hast nicht genügend Geld.");
                        return;
                    }
                    coinsUser.removeCoins(finalAmount * Bukkit.getOnlinePlayers().size());
                    for (Player target : Bukkit.getOnlinePlayers())
                        if (target != player)
                            main.getCoinUserManager().getCoinsUser(target.getUniqueId()).thenAccept(targetCoinsUser -> {
                                targetCoinsUser.addCoins(finalAmount);
                                main.getCoinUserManager().saveCoinsUser(targetCoinsUser);
                                player.sendMessage(main.getPrefix() + "Du hast §b" + target.getName() + " " + finalAmount + "€ §7gegeben.");
                                target.sendMessage(main.getPrefix() + "Du hast von §b" + player.getName() + " " + finalAmount + "€ §7erhalten.");
                            });
                    main.getCoinUserManager().saveCoinsUser(coinsUser);
                });
                return false;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(main.getPrefix() + "Der Spieler §b" + args[0] + " §7ist nicht online.");
                return false;
            }
            if (target == player) {
                player.sendMessage(main.getPrefix() + "Du kannst dir selbst kein Geld geben.");
                return false;
            }
            main.getCoinUserManager().getCoinsUser(player.getUniqueId()).thenAccept(coinsUser -> {
                if (coinsUser.getCoins() < finalAmount) {
                    player.sendMessage(main.getPrefix() + "Du hast nicht genügend Geld.");
                    return;
                }
                main.getCoinUserManager().getCoinsUser(target.getUniqueId()).thenAccept(targetCoinsUser -> {
                    coinsUser.removeCoins(finalAmount);
                    targetCoinsUser.addCoins(finalAmount);
                    player.sendMessage(main.getPrefix() + "Du hast §b" + target.getName() + " " + finalAmount + "€ §7gegeben.");
                    target.sendMessage(main.getPrefix() + "Du hast von §b" + player.getName() + " " + finalAmount + "€ §7erhalten.");
                    main.getCoinUserManager().saveCoinsUser(coinsUser);
                    main.getCoinUserManager().saveCoinsUser(targetCoinsUser);
                });
            });
        } else
            player.sendMessage(main.getPrefix() + "Bitte benutze: /pay <Spieler> <Betrag>");

        return false;
    }
}

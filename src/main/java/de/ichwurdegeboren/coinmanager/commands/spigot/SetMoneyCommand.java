package de.ichwurdegeboren.coinmanager.commands.spigot;

import de.ichwurdegeboren.coinmanager.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetMoneyCommand implements CommandExecutor {

    private final Main main;

    public SetMoneyCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(main.getPrefix() + "Du musst ein Spieler sein, um diesen Befehl nutzen zu können.");
            return false;
        }

        Player player = (Player) sender;

        if (player.hasPermission("moneysystem.setmoney")) {
            if (args.length == 2) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    player.sendMessage(main.getPrefix() + "Der Spieler §b" + args[0] + " §7ist nicht online.");
                    return false;
                }

                double amount = -1;
                try {
                    amount = Double.parseDouble(args[1]);
                } catch (NumberFormatException e) {
                    player.sendMessage(main.getPrefix() + "Bitte benutze: /setmoney <Spieler> <Betrag>");
                    return false;
                }

                if (amount < 0.01 && amount != 0) {
                    player.sendMessage(main.getPrefix() + "Der angegebene Geldbetrag ist zu klein.");
                    return false;
                }
                final double finalAmount = amount;

                main.getCoinUserManager().getCoinsUser(target.getUniqueId()).thenAccept(coinUser -> {
                    coinUser.setCoins(finalAmount).thenAccept(bool -> {
                        if (!bool) return;
                        main.getCoinUserManager().saveCoinsUser(coinUser);
                        target.sendMessage(main.getPrefix() + "Dir wurden §b" + finalAmount + "€ §7gesetzt.");
                        player.sendMessage(main.getPrefix() + "Du hast §b" + target.getName() + " " + finalAmount + "€ §7gesetzt.");
                    });
                });
            } else
                player.sendMessage(main.getPrefix() + "Bitte benutze: /setmoney <Spieler> <Betrag>");
        } else
            player.sendMessage(main.getPrefix() + "Dazu hast du keine Rechte.");

        return false;
    }
}

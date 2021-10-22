package de.ichwurdegeboren.coinmanager.commands.spigot;

import de.ichwurdegeboren.coinmanager.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MoneyCommand implements CommandExecutor {

    private final Main main;

    public MoneyCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(main.getPrefix() + "Du musst ein Spieler sein, um diesen Befehl nutzen zu können.");
            return false;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            main.getCoinUserManager().getCoinsUser(player.getUniqueId()).thenAccept(coinUser -> player.sendMessage(main.getPrefix() + "Du hast aktuell §b" + coinUser.getCoins() + "€§7."));
            return true;
        }
        if (args.length == 1) {
            if (player.hasPermission("moneysystem.money.other")) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    player.sendMessage(main.getPrefix() + "Der Spieler §b" + args[0] + " §7ist nicht online.");
                    return false;
                }
                main.getCoinUserManager().getCoinsUser(target.getUniqueId()).thenAccept(coinUser -> player.sendMessage(main.getPrefix() + "§b" + target.getName() + " §7hat aktuell §b" + coinUser.getCoins() + "€§7."));
            } else
                player.sendMessage(main.getPrefix() + "Dazu hast du keine Rechte.");
        } else
            player.sendMessage(main.getPrefix() + "Bitte benutze: /" + label + "" + (player.hasPermission("moneysystem.money.other") ? " <Spieler>" : ""));

        return false;
    }
}

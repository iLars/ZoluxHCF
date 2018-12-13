package cc.zolux.hcf.commands;

import cc.zolux.hcf.HCF;
import cc.zolux.hcf.faction.FactionPlayer;
import cc.zolux.hcf.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            FactionPlayer fp = HCF.getPlugin().factionManager.getPlayer(player.getUniqueId());
            if(args.length == 0) {
                player.sendMessage(Color.c("&9&lZolux &7» &7Your balance is: " + fp.getBalance()));
            } else {
                if(args.length > 1) {
                    player.sendMessage(Color.c("&9&lZolux &7» /" + label + " [player]"));
                }
                if(args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    FactionPlayer factionPlayer = HCF.getPlugin().factionManager.getPlayer(target.getUniqueId());
                    if(factionPlayer == null) {
                        player.sendMessage(Color.c("&9&lZolux &7» " + target.getName() + " has never joined the server."));
                    } else {
                        player.sendMessage(Color.c("&9&lZolux &7» Balance of " + target.getName() + ": " + factionPlayer.getBalance()));
                    }
                }
            }
        }
        return false;
    }
}

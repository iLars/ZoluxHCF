package cc.zolux.hcf.events;

import cc.zolux.hcf.HCF;
import cc.zolux.hcf.faction.FactionPlayer;
import cc.zolux.hcf.utils.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void on(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        FactionPlayer fp = HCF.getPlugin().factionManager.getPlayer(player.getUniqueId());
        if(fp == null) {
            fp = new FactionPlayer(player, 200, null);
            HCF.getPlugin().factionManager.addPlayer(fp);
            HCF.getPlugin().factionManager.createProfile(player);
            player.sendMessage(Color.c("&7&m------------"));
            player.sendMessage(Color.c("&9&lZolux &7| &7HCF"));
            player.sendMessage(Color.c("&7Welcome to HCF!"));
            player.sendMessage(Color.c("&7Use /factions to get started."));
            player.sendMessage(Color.c("&7&m------------"));
        }
    }
}

package cc.zolux.hcf.events;

import cc.zolux.hcf.HCF;
import cc.zolux.hcf.faction.FactionPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void on(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        FactionPlayer fp = HCF.getPlugin().factionManager.getPlayer(player.getUniqueId());
        HCF.getPlugin().factionManager.saveProfile(player.getUniqueId());
    }
}

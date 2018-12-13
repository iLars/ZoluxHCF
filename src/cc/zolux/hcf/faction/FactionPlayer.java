package cc.zolux.hcf.faction;

import cc.zolux.hcf.HCF;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.util.UUID;

public class FactionPlayer {
    private UUID uuid;
    private int balance;
    private Faction faction;
    private FactionRole role = FactionRole.MEMBER;

    public FactionPlayer(Player player, int balance, Faction faction) {
        this.uuid = player.getUniqueId();
        this.balance = balance;
        this.faction = faction;
    }
    public FactionPlayer(ResultSet set) throws Exception{
        this.uuid = UUID.fromString(set.getString("uuid"));
        this.balance = set.getInt("balance");
        this.faction = HCF.getPlugin().factionManager.getFaction(set.getString("faction"));
        this.role = FactionRole.values()[set.getInt("role")];

    }

    public int getBalance() {
        return balance;
    }

    public Faction getFaction() {
        return faction;
    }

    public UUID getUuid() {
        return uuid;
    }
    public FactionPlayer(ResultSet set, FactionManager manager) throws Exception {
        this.uuid = UUID.fromString(set.getString("uuid"));
        this.balance = set.getInt("balance");
        this.faction = HCF.getPlugin().factionManager.getFaction(set.getString("faction")); //TODO: getFaction();
    }
    public String getName() {
        return Bukkit.getOfflinePlayer(uuid).getName();
    }
    public String getFactionName() {
        return faction == null ? "null" : faction.getName();
    }

    public void setFaction(Faction faction) {
        this.faction = faction;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public FactionRole getRole() {
        return role;
    }

    public void setRole(FactionRole role) {
        this.role = role;
    }
}

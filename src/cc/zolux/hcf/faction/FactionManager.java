package cc.zolux.hcf.faction;

import cc.zolux.hcf.HCF;
import cc.zolux.hcf.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;

public class FactionManager {

    private HCF hcf;
    public static ArrayList<Faction> FACTIONS = new ArrayList();
    public static ArrayList<FactionPlayer> PLAYERS = new ArrayList();
    private Statement statement;

    public FactionManager(HCF plugin) {
        this.hcf = plugin;

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    int factions = 0;
                    int players = 0;
                    ResultSet set = getStatement().executeQuery("SELECT * FROM factions");
                    while (set.next()) {
                        factions++;
                        Faction faction = new Faction(set.getString("name"), set.getString("description"));
                        faction.setDtrChangeReason(DTRChangeReason.NO_CHANGE);
                        faction.setRegening(false);
                        FACTIONS.add(faction);

                    }
                    set = getStatement().executeQuery("SELECT * FROM players");
                    while (set.next()) {
                        players++;
                        PLAYERS.add(new FactionPlayer(set));
                    }
                    Bukkit.getConsoleSender().sendMessage(Color.c("&9Zolux found " + factions + " factions and " + players + " players."));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(hcf);
    }

    public FactionPlayer getPlayer(UUID uuid) {
        for (FactionPlayer fp : PLAYERS)
            if (fp.getUuid().equals(uuid)) {
                return fp;
            }
        return null;
    }

    public Faction getFaction(String name) {
        for (Faction faction : FACTIONS)
            if (faction.getName().equalsIgnoreCase(name))
                return faction;
        return null;
    }

    public Statement getStatement() throws SQLException {
        return statement == null || statement.isClosed() ? statement = HCF.getPlugin().getMySQLManager().getConnection().createStatement() : statement;
    }

    public void createProfile(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    FactionPlayer fp = getPlayer(player.getUniqueId());
                    PreparedStatement preparedStatement = HCF.getPlugin().mySQLManager.getConnection().prepareStatement("INSERT INTO players (uuid,balance,faction,role) VALUES (?,?,?,?)");
                    preparedStatement.setString(1, player.getUniqueId().toString());
                    preparedStatement.setInt(2, 200);
                    preparedStatement.setString(3, null);
                    preparedStatement.setInt(4, 0);
                    preparedStatement.executeUpdate();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(HCF.getPlugin());
    }

    public void saveProfile(UUID uuid) {
        try {
            FactionPlayer fp = getPlayer(uuid);
            PreparedStatement preparedStatement = HCF.getPlugin().mySQLManager.getConnection().prepareStatement("UPDATE `players` SET balance='" + fp.getBalance() + "', faction='" + fp.getFactionName() + "', role='" + fp.getRole().getId() + "' WHERE uuid='" + uuid + "'");
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disable() {
        new Thread(() -> {
            for (FactionPlayer fp : PLAYERS) {
                saveProfile(fp.getUuid());
            }
            for (Faction f : FACTIONS) {
                saveFaction(f);
            }
            HCF.getPlugin().getMySQLManager().closeConnection();
        }, "disable").start();

    }

    public void saveFaction(Faction f) {
        try {
            PreparedStatement preparedStatement = HCF.getPlugin().mySQLManager.getConnection().prepareStatement("UPDATE `factions` SET description='" + f.getDescription() + "', location='" + f.getHomeSerialized() + "', dtr='" + f.getCurrentDTR() + "' WHERE name='" + f.getName() + "'");
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addPlayer(FactionPlayer fp) {
        PLAYERS.add(fp);
    }

    public void addFaction(Faction faction) {
        FACTIONS.add(faction);
    }

    public void insertFaction(Faction f) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement preparedStatement = HCF.getPlugin().mySQLManager.getConnection().prepareStatement("INSERT INTO factions (name,description,location,dtr) VALUES (?,?,?,?)");
                    preparedStatement.setString(1, f.getName());
                    preparedStatement.setString(2, f.getDescription());
                    preparedStatement.setString(3, f.getHomeSerialized());
                    preparedStatement.setDouble(4, f.getMaxDTR());
                    preparedStatement.executeUpdate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(HCF.getPlugin());
    }
    public static ArrayList<FactionPlayer> getPlayers() {
        return PLAYERS;
    }
    public static ArrayList<FactionPlayer> getPlayersFromFaction(Faction faction) {
        ArrayList<FactionPlayer> players = new ArrayList<>();
        for(FactionPlayer fp : PLAYERS) {
            if(fp.getFaction().equals(faction)) {
                players.add(fp);
            }
        }
        return players;
    }
}

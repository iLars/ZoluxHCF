package cc.zolux.hcf.faction;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.sql.ResultSet;

public class Faction {

    private String name, description;
    private Location home;
    private double currentDTR;
    private boolean regening;
    private DTRChangeReason dtrChangeReason;

    public Faction(String name, String description) {
        this.name = name;
        this.description = description;
        this.home = null;
        this.currentDTR = 0.0;
    }
    public Faction(ResultSet set) throws Exception {
        this.name = set.getString("name");
        this.description = set.getString("description");
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Faction ? obj == this || ((Faction) obj).getName().equalsIgnoreCase(name): false;
    }

    public void setHome(Location home) {
        this.home = home;
    }

    public Location getHome() {
        return home;
    }

    public double getMaxDTR() {
        if(FactionManager.getPlayersFromFaction(this).size() == 1) {
            return 1.1;
        }
        if(FactionManager.getPlayersFromFaction(this).size() == 2) {
            return 2.1;
        }
        return Math.min(7.5, FactionManager.getPlayersFromFaction(this).size() * 0.9);
    }

    public void setCurrentDTR(double currentDTR) {
        this.currentDTR = currentDTR;
    }
    public String getHomeSerialized() {
        return home == null ? "null" : home.getWorld() + ":" + home.getX() + ":" + home.getY() + ":" + home.getZ();
    }
    public void setHomeSerialized(String string) {
        String[] s = string.split(":");
        World w = Bukkit.getServer().getWorld(s[0]);
        double x = Double.parseDouble(s[1]);
        double y = Double.parseDouble(s[2]);
        double z = Double.parseDouble(s[3]);
        this.home = new Location(w, x, y, z);
    }

    public double getCurrentDTR() {
        return currentDTR;
    }

    public void setRegening(boolean regening) {
        this.regening = regening;
    }

    public boolean isRegening() {
        return regening;
    }

    public DTRChangeReason getDtrChangeReason() {
        return dtrChangeReason;
    }

    public void setDtrChangeReason(DTRChangeReason dtrChangeReason) {
        this.dtrChangeReason = dtrChangeReason;
    }
}

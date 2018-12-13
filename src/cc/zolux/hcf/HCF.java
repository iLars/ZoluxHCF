package cc.zolux.hcf;

import cc.zolux.hcf.commands.BalanceCommand;
import cc.zolux.hcf.commands.FactionCommand;
import cc.zolux.hcf.connection.FileManager;
import cc.zolux.hcf.connection.MySQLManager;
import cc.zolux.hcf.events.PlayerJoinListener;
import cc.zolux.hcf.events.PlayerQuitListener;
import cc.zolux.hcf.faction.FactionManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class HCF extends JavaPlugin {

    public static HCF plugin;
    public FileManager fileManager;
    public MySQLManager mySQLManager;
    public FactionManager factionManager;

    @Override
    public void onEnable() {
        plugin = this;

        fileManager = new FileManager(this);
        mySQLManager = new MySQLManager();
        factionManager = new FactionManager(this);


        getCommand("balance").setExecutor(new BalanceCommand());
        getCommand("faction").setExecutor(new FactionCommand());


        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), this);


        fileManager.getConfig("config.yml").copyDefaults(true);
        mySQLManager.openConnection();
    }

    @Override
    public void onDisable() {
        factionManager.disable();
    }

    public static HCF getPlugin() {
        return plugin;
    }
    public FileManager getFileManager() {
        return fileManager;
    }
    public MySQLManager getMySQLManager() {
        return mySQLManager;
    }

}

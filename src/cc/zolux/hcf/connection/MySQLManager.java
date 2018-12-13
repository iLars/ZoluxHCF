package cc.zolux.hcf.connection;

import cc.zolux.hcf.HCF;
import cc.zolux.hcf.utils.Color;
import com.huskehhh.mysql.mysql.MySQL;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQLManager {

    private static MySQL MYSQL;
    public String host, database, username, password, port;

    private static Connection connection;

    public void openConnection() {
        host = HCF.getPlugin().getFileManager().getConfig("config.yml").get().getString("connection.host");
        database = HCF.getPlugin().getFileManager().getConfig("config.yml").get().getString("connection.database");
        username = HCF.getPlugin().getFileManager().getConfig("config.yml").get().getString("connection.username");
        password = HCF.getPlugin().getFileManager().getConfig("config.yml").get().getString("connection.password");
        port = HCF.getPlugin().getFileManager().getConfig("config.yml").get().getString("connection.port");

        MYSQL = new MySQL(host, port, database, username, password);

        try {
            connection = MYSQL.openConnection();
            Bukkit.getConsoleSender().sendMessage(Color.c("&9Zolux succesfully connected to our database. &7[" + Bukkit.getServer().getVersion() + "]"));

            PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS players(uuid varchar(64), balance int, faction varchar(64), role int)");
            preparedStatement.executeUpdate();

            PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS factions(name varchar(64), description varchar(64), location varchar(64), dtr double)");
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            } else {
                openConnection();
                return connection;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void closeConnection() {
        try {
            MYSQL.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

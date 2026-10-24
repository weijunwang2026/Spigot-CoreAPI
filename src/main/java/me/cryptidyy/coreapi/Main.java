package me.cryptidyy.coreapi;

import me.cryptidyy.coreapi.api.API;
import me.cryptidyy.coreapi.api.JoinHandler;
import me.cryptidyy.coreapi.api.StatusUpdater;
import me.cryptidyy.coreapi.data.DataManager;
import me.cryptidyy.coreapi.spigotcommands.CommandSQL;
import me.cryptidyy.coreapi.spigotcommands.SpigotCommand;
import me.cryptidyy.coreapi.sql.SQLConnection;
import me.cryptidyy.coreapi.sql.StatusUpdaterSQL;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class Main extends JavaPlugin {

    private static Main instance;
    public SQLConnection sqlConnection;
    private CommandSQL cmdSQL;
    private DataManager data = new DataManager(this, "settings.yml");
    private StatusUpdater statusUpdater;

    @Override
    public void onEnable() {
        this.getDataFolder().mkdir();
        instance = this;
        try {
            sqlConnection = new SQLConnection(this);
        } catch (Exception e) {
            getLogger().severe("Please provide sql login information in mysql.yml! Shutting down...");
            onDisable();
            return;
        }

        try {
            connectToSQL();
        } catch (Exception e) {
            getLogger().severe("Cannot connect to database. Make sure your database is running and your login " +
                    "information in mysql.yml is correct");
            getLogger().severe("Shutting down...");
            onDisable();
            return;
        }

        new API();
        statusUpdater = new StatusUpdaterSQL();

        this.cmdSQL = new CommandSQL(this);
        if(data.getConfig().getString("serverid").contains("lobby"))
            this.getCommand("spigotcmd").setExecutor(new SpigotCommand(cmdSQL));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        statusUpdater.setStarted(false);
        statusUpdater.setWaiting(false);
    }

    public static Main getInstance()
    {
        return instance;
    }

    private void connectToSQL() throws Exception
    {
        sqlConnection.connect();

        if(sqlConnection.isConnected())
        {
            Bukkit.getLogger().info("Database is connected!");
        }
    }

    public SQLConnection getSQLConnection()
    {
        return sqlConnection;
    }
}

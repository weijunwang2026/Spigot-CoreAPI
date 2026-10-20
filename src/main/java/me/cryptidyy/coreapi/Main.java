package me.cryptidyy.coreapi;

import me.cryptidyy.coreapi.api.API;
import me.cryptidyy.coreapi.api.JoinHandler;
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


    @Override
    public void onEnable() {
        instance = this;

        sqlConnection = new SQLConnection();
        connectToSQL();

        new API();
        new StatusUpdaterSQL();

        this.cmdSQL = new CommandSQL(this);
        if(data.getConfig().getString("serverid").contains("lobby"))
            this.getCommand("spigotcmd").setExecutor(new SpigotCommand(cmdSQL));


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Main getInstance()
    {
        return instance;
    }

    private void connectToSQL()
    {
        try
        {
            sqlConnection.connect();
        }
        catch (ClassNotFoundException | SQLException e)
        {
            //Login info incorrect
            //not using database

            Bukkit.getLogger().info("Database not connected!");
        }

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

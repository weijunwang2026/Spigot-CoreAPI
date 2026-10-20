package me.cryptidyy.coreapi.api;

import me.cryptidyy.coreapi.Main;
import me.cryptidyy.coreapi.data.DataManager;
import me.cryptidyy.coreapi.spigotcommands.CommandSQL;
import me.cryptidyy.coreapi.sql.GameJoinerSQL;
import me.cryptidyy.coreapi.sql.QueuesSQL;
import me.cryptidyy.coreapi.sql.StatusUpdaterSQL;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
public class API {

    private DataManager settingsData = new DataManager(Main.getInstance(), "settings.yml");
    private StatusUpdater statusUpdater = new StatusUpdaterSQL();
    private static API instance;
    public List<UUID> alreadyQueuedPlayers = new ArrayList<>();
    private QueuesSQL queuesSQL = new QueuesSQL();
    private CommandSQL cmdSQL = new CommandSQL(Main.getInstance());

    public static API getInstance()
    {
        return instance;
    }

    public API()
    {
        instance = this;
    }

    //Called by the individual game plugins
    public List<GameJoiner> checkQueue()
    {
        List<UUID> queuedUUID = queuesSQL.checkQueue();
        List<GameJoiner> result = new ArrayList<>();

        for(UUID uuid : queuedUUID)
        {
            GameJoiner joiner = new GameJoinerSQL(uuid);

            if(uuid == null || alreadyQueuedPlayers.contains(uuid))
            {
                continue;
            }

            if(settingsData.getConfig().getStringList("bannedplayers").contains(uuid))
            {
                //set can_join to false
                joiner.setAllowJoin(false);
                continue;
            }
            result.add(joiner);
        }

        return result;
    }

    //Called by the game plugin when a queued player is checked
    public void setChecked(UUID uuid)
    {
        this.alreadyQueuedPlayers.add(uuid);
    }

    public StatusUpdater getStatusUpdater()
    {
        return statusUpdater;
    }
    public String getServerID()
    {
        return settingsData.getConfig().getString("serverid");
    }
    public int getMaxPlayerNum()
    {
        return settingsData.getConfig().getInt("maxplayernum");
    }

    public void queueGame(UUID uuid, String gameName)
    {
        cmdSQL.sendCommand(Bukkit.getPlayer(uuid), "join " + gameName);
    }

}

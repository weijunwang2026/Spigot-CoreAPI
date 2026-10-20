package me.cryptidyy.coreapi.sql;

import de.simonsator.partyandfriends.spigot.api.pafplayers.PAFPlayer;
import de.simonsator.partyandfriends.spigot.api.pafplayers.PAFPlayerManager;
import de.simonsator.partyandfriends.spigot.api.party.PartyManager;
import de.simonsator.partyandfriends.spigot.api.party.PlayerParty;
import me.cryptidyy.coreapi.Main;
import me.cryptidyy.coreapi.api.API;
import me.cryptidyy.coreapi.api.GameJoiner;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameJoinerSQL implements GameJoiner {
    private SQLConnection sqlConnection = Main.getInstance().sqlConnection;
    private UUID uuid;
    public GameJoinerSQL(UUID uuid)
    {
        this.uuid = uuid;
    }

    @Override
    public UUID getUUID()
    {
        return this.uuid;
    }

    @Override
    public PlayerParty getParty() {
        PAFPlayer pPlayer = PAFPlayerManager.getInstance().getPlayer(uuid);
        PlayerParty party = PartyManager.getInstance().getParty(pPlayer);

        return party;
    }

    @Override
    public List<UUID> getAllPlayers() {

        List<UUID> allPlayers = new ArrayList<>();
        if(getParty() == null)
        {
            allPlayers.add(uuid);
            return allPlayers;
        }

        getParty().getAllPlayers().stream().map(PAFPlayer::getUniqueId).forEach(uuid -> {
            allPlayers.add(uuid);
            System.out.println(uuid);
        });
        return allPlayers;

    }

    @Override
    public void setPlayerConnected(boolean isConnected)
    {
        String tablePrefix = sqlConnection.getTABLE_PREFIX();
        Connection con = sqlConnection.getConnection();
        PreparedStatement prepStmt = null;

        try {
            prepStmt = con.prepareStatement("UPDATE " + tablePrefix + "player_queues SET is_connected = ? WHERE (player_id = ?)");
            prepStmt.setBoolean(1, isConnected);
            prepStmt.setInt(2, getPlayerID());
            prepStmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setAllowJoin(boolean canJoin)
    {
        //if called (there's a response), or filtered here, update can_join value
        Connection con = sqlConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE " + sqlConnection.getTABLE_PREFIX()
                    + "servers_can_join SET can_join = ? WHERE (player_id = ? AND server_id = ?)");
            ps.setBoolean(1, canJoin);
            ps.setInt(2, getPlayerID());
            ps.setString(3, API.getInstance().getServerID());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //Bukkit.broadcastMessage(getName() + " allowed to join: " + canJoin);
    }

    @Override
    public void setRejectedReason(String reason)
    {
        Connection con = sqlConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE " + sqlConnection.getTABLE_PREFIX()
                    + "servers_can_join SET reason = ? WHERE (player_id = ? AND server_id = ?)");
            ps.setString(1, reason);
            ps.setInt(2, getPlayerID());
            ps.setString(3, API.getInstance().getServerID());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //called when player successfully joins the game
    @Override
    public void removeFromQueue() {
        //remove from servers_can_join, player_queues, and queues_in_servers
        API.getInstance().alreadyQueuedPlayers.remove(uuid);
    }

    @Override
    public String getName() {
        String playerName;
        Connection con = sqlConnection.getConnection();
        Statement stmt = null;
        ResultSet rs = null;

        try {
            rs = (stmt = con.createStatement()).executeQuery("SELECT name, uuid FROM " + sqlConnection.getTABLE_PREFIX()
                    + "player_info WHERE uuid='" + uuid + "' LIMIT 1");
            if (rs.next())
            {
                playerName = rs.getString("name");
                return playerName;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return "";
    }

    private int getPlayerID()
    {
        String tablePrefix = sqlConnection.getTABLE_PREFIX();
        Connection con = sqlConnection.getConnection();
        PreparedStatement ps = null;
        int playerID = 0;

        try {
            ps = con.prepareStatement("SELECT player_id FROM " + tablePrefix + "player_info WHERE uuid = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next())
            {
                playerID = rs.getInt("player_id");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return playerID;
    }
}

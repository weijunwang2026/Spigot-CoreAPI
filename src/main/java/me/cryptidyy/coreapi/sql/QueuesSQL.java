package me.cryptidyy.coreapi.sql;

import me.cryptidyy.coreapi.Main;
import me.cryptidyy.coreapi.api.API;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class QueuesSQL {

    private final String TABLE_PREFIX = "cc_";

    private SQLConnection sqlConnection = Main.getInstance().sqlConnection;

    public UUID getUUID(int playerID)
    {
        UUID uuid;
        Connection con = sqlConnection.getConnection();
        Statement stmt = null;
        ResultSet rs = null;

        try
        {
            rs = (stmt = con.createStatement()).executeQuery("SELECT uuid, name FROM " + TABLE_PREFIX
                    + "player_info WHERE player_id='" + playerID + "' LIMIT 1");
            if (rs.next())
            {
                uuid = UUID.fromString(rs.getString("uuid"));
                return uuid;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            //close(con, rs, stmt);
        }
        return null;
    }

    public boolean isInQueue(UUID uuid)
    {
        String serverID = API.getInstance().getServerID();
        Connection con = sqlConnection.getConnection();

        try {
            PreparedStatement ps = con.prepareStatement("SELECT EXISTS(SELECT * FROM " + TABLE_PREFIX + "player_queues pq INNER JOIN " + TABLE_PREFIX + "player_info pi WHERE (pq.player_id = pi.player_id AND uuid = ? AND server_id = ?))");
            ps.setString(1, uuid.toString());
            ps.setString(2, serverID);

            ResultSet rs = ps.executeQuery();
            if(rs.next())
            {
                return rs.getBoolean(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    //check all queues for this server
    public List<UUID> checkQueue()
    {
        List<UUID> queuedPlayers = new ArrayList<>();
        //remove from alreadyInQueue list if player isn't in queue anymore
        for(UUID uuid : API.getInstance().alreadyQueuedPlayers)
        {
            if(!isInQueue(uuid))
            {
                API.getInstance().alreadyQueuedPlayers.remove(uuid);
                break;
            }
        }

        String serverID = API.getInstance().getServerID();
        int playerID = 0;

        Connection con = sqlConnection.getConnection();

        try {
            PreparedStatement ps =
                    con.prepareStatement("SELECT player_id FROM "
                            + TABLE_PREFIX + "player_queues WHERE(server_id = ?)");
            ps.setString(1, serverID);
            ResultSet rs = ps.executeQuery();

            //gets first player in queue
            while(rs.next())
            {
                playerID = rs.getInt("player_id");
                if(playerID != 0)
                {
                    queuedPlayers.add(getUUID(playerID));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return queuedPlayers;

    }
}

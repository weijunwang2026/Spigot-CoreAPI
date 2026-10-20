package me.cryptidyy.coreapi.sql;

import me.cryptidyy.coreapi.Main;
import me.cryptidyy.coreapi.api.API;
import me.cryptidyy.coreapi.api.StatusUpdater;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StatusUpdaterSQL implements StatusUpdater {

    private SQLConnection sqlConnection = Main.getInstance().sqlConnection;


    @Override
    public void setStarted(boolean isStarted)
    {
        Connection con = sqlConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE " + sqlConnection.getTABLE_PREFIX() + "server_status SET is_started = ? WHERE server_id = ?");
            ps.setBoolean(1, isStarted);
            ps.setString(2, API.getInstance().getServerID());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setWaiting(boolean isWaiting)
    {
        Connection con = sqlConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE " + sqlConnection.getTABLE_PREFIX() + "server_status SET is_waiting = ? WHERE server_id = ?");
            ps.setBoolean(1, isWaiting);
            ps.setString(2, API.getInstance().getServerID());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setJoinedPlayerNum(int playerNum)
    {
        Connection con = sqlConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE " + sqlConnection.getTABLE_PREFIX() + "server_status SET total_player_num = ? WHERE server_id = ?");
            ps.setInt(1, playerNum);
            ps.setString(2, API.getInstance().getServerID());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void registerMaxPlayerNum()
    {
        int maxPlayerNum = API.getInstance().getMaxPlayerNum();
    }
}

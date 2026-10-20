package me.cryptidyy.coreapi.spigotcommands;

import me.cryptidyy.coreapi.Main;
import me.cryptidyy.coreapi.sql.SQLConnection;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class CommandSQL {

    private SQLConnection con;
    private final String TABLE_PREFIX = "cc_";
    public CommandSQL(Main plugin)
    {
        this.con = plugin.getSQLConnection();
    }

    public void sendCommand(Player player, String cmd)
    {
        PreparedStatement stmt;
        try
        {
           stmt = con.getConnection().prepareStatement("INSERT INTO " + TABLE_PREFIX + "commands VALUES (?, ?, 0);");
           stmt.setString(1, player.getUniqueId().toString());
           stmt.setString(2, cmd);
           stmt.executeUpdate();
           stmt.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public boolean hasCommand(Player player)
    {
        PreparedStatement stmt;
        ResultSet rs;

        try
        {
            stmt = con.getConnection()
                    .prepareStatement("SELECT uuid FROM " + TABLE_PREFIX + "commands;");
            rs = stmt.executeQuery();
            while(rs.next())
            {
                if(rs.getString("uuid").equalsIgnoreCase(player.getUniqueId().toString()))
                    return true;
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }

}

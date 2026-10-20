package me.cryptidyy.coreapi.spigotcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpigotCommand implements CommandExecutor {

    private CommandSQL sql;
    public SpigotCommand(CommandSQL sql)
    {
        this.sql = sql;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(label.equalsIgnoreCase("spigotcmd"))
        {
            if(!(sender instanceof Player)) return true;
            Player player = (Player) sender;

            if(sql.hasCommand(player))
            {
                player.sendMessage(ChatColor.RED + "Please wait before executing another command!");
                return true;
            }

            if(args.length < 2)
            {
                player.sendMessage(ChatColor.RED + "Please specify the bungee command!");
                return true;
            }

            String bungeeCmd = commandFromArgs(args);
            sql.sendCommand(player, bungeeCmd);
        }

        return false;
    }

    private String commandFromArgs(String[] args)
    {
        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i < args.length; i++)
        {
            String arg = args[i];
            stringBuilder.append(arg);

            if(i != args.length - 1)
            {
                stringBuilder.append(" ");
            }

        }

        return stringBuilder.toString();
    }
}

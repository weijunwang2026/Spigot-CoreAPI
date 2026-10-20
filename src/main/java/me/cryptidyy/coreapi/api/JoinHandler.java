package me.cryptidyy.coreapi.api;

import me.cryptidyy.coreapi.Main;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class JoinHandler implements Listener {
    private Server server;
    private QueueListener queueListener;

    public JoinHandler(QueueListener queueListener, Server server)
    {
        this.queueListener = queueListener;
        this.server = server;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();

        if(getLobbyPlayers().contains(player.getUniqueId()))
        {
            System.out.println(ChatColor.RED + "Player " + player.getName() + " has already joined the game!");
            //return;
        }

        event.setJoinMessage(ChatColor.AQUA + "[CoreAPI] " + ChatColor.GRAY + player.getName() + " has joined. (" + (getLobbyPlayers().size() + 1) + ")");

        if(!queueListener.getQueuedPlayers().stream().map(GameJoiner::getUUID).anyMatch(player.getUniqueId()::equals))
        {
            //allow party members to join still
            System.out.println(ChatColor.RED + player.getName() + " joined without queue!");
        }

        GameJoiner gameJoiner = queueListener.getQueuedPlayers().stream().filter(joiner -> joiner.getUUID().equals(player.getUniqueId()))
                .findFirst().get();

        gameJoiner.setPlayerConnected(true);
        join(gameJoiner);
        //remove joiner from queue once it has joined
        gameJoiner.removeFromQueue();
        API.getInstance().alreadyQueuedPlayers.remove(gameJoiner.getUUID());
    }

    public abstract List<UUID> getLobbyPlayers();
    public abstract void join(GameJoiner joiner);

    public void setListening(boolean isListening)
    {
        queueListener.setListening(isListening);
        if(isListening) {
            HandlerList.unregisterAll(this);
            registerEvent();
        }
        else {
            unregisterEvent();
        }
    }
    public void registerEvent()
    {
        server.getPluginManager().registerEvents(this, Main.getInstance());
    }
    public void unregisterEvent()
    {
        HandlerList.unregisterAll(this);
    }

}

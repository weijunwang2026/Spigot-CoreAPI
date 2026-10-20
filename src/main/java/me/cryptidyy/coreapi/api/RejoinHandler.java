package me.cryptidyy.coreapi.api;

import me.cryptidyy.coreapi.Main;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;
import java.util.UUID;

public abstract class RejoinHandler implements Listener {

    private Server server;
    private QueueListener queueListener;
    public RejoinHandler(QueueListener queueListener, Server server)
    {
        this.queueListener = queueListener;
        this.server = server;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        if(!getAllPlayers().contains(player.getUniqueId())) return;
        event.setJoinMessage("");

        GameJoiner joiner =
                queueListener.getQueuedPlayers()
                        .stream()
                        .filter(queued -> queued.getUUID().equals(player.getUniqueId()))
                        .findFirst()
                        .get();

        if(joiner == null)
        {
            player.sendMessage("You joined without a queue!");
            return;
        }

        rejoin(joiner);
        joiner.setPlayerConnected(true);
    }

    public abstract List<UUID> getAllPlayers();
    public abstract void rejoin(GameJoiner joiner);

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

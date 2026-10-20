package me.cryptidyy.coreapi.api;

import me.cryptidyy.coreapi.sql.GameJoinerSQL;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public abstract class QueueListener extends BukkitRunnable {
    private boolean isListening = false;

    private List<GameJoiner> queuedPlayers = new ArrayList<>();

    @Override
    public void run()
    {
        if(!isListening) return;
        //check players from queue whether they can join, and update the according tables
        List<GameJoiner> joiners = API.getInstance().checkQueue();
        for(GameJoiner joiner : joiners)
        {
            if(joiner == null) return;
            if(!canJoin(joiner))
            {
                joiner.setAllowJoin(false);
                return;
            }

            //Also add members in the party of the joiner to the queue list
            joiner.getAllPlayers().forEach(member -> {
                GameJoiner memberJoiner = new GameJoinerSQL(member);
                queuedPlayers.add(memberJoiner);
            });

            //Communicate with CoreAPI on which players are already checked
            API.getInstance().setChecked(joiner.getUUID());
            joiner.setAllowJoin(true);
        }

    }

    public List<GameJoiner> getQueuedPlayers() {
        return queuedPlayers;
    }
    public abstract boolean canJoin(GameJoiner joiner);
    public void setListening(boolean isListening)
    {
        this.isListening = isListening;
    }
}

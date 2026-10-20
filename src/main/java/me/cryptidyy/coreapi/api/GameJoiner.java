package me.cryptidyy.coreapi.api;

import de.simonsator.partyandfriends.spigot.api.party.PlayerParty;

import java.util.List;
import java.util.UUID;

public interface GameJoiner {

    UUID getUUID();

    PlayerParty getParty();

    List<UUID> getAllPlayers();

    void setPlayerConnected(boolean isConnected);

    void setAllowJoin(boolean canJoin);

    void setRejectedReason(String reason);

    void removeFromQueue();

    String getName();

}

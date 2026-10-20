package me.cryptidyy.coreapi.api;

import java.util.UUID;

public interface StatusUpdater {

    void setStarted(boolean isStarted);

    void setWaiting(boolean isWaiting);

    void setJoinedPlayerNum(int playerNum);

    void registerMaxPlayerNum();
}

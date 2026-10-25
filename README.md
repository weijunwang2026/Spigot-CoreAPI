# Spigot-CoreAPI #

## The Spigot side for the Bungee-Core-Plugin that communicates with Spigot plugins on game servers ##

This API provides methods for minigame plugins to handle player/party joining, server status updating, and a connection to MySQL. Some features include:

* Listening and handling player queues, allows/rejects a queue based on predicates defined by the minigame plugin
* Allows player to rejoin a game after a disconnection
* Allows the minigame plugin to handle parties
* Updates the game server and player vqueue status to communicate with the Bungee-Core-Plugin

## For Developers ##

### Handling player queueing ###

```java
public class MyQueueListener extends QueueListener {

  @Override
  public boolean canJoin(GameJoiner potentialJoiner) { //Restrictions on who can join, such as on party size
    ...
  }

}
```

### Handling player/parties joining ###

```java
public class MyJoinHandler extends JoinHandler {
  public MyJoinHandler(MyQueueListener queueListener, Server server) {
    super(queueListener, server);
  }

  @Override
  public List<UUID> getLobbyPlayers() { //Return the stored list of players in the lobby
    ...
  }

  @Override
  public void join(GameJoiner joiner) { //This method is called for every player that joins the lobby and upon rejoining the game
    PlayerParty party = joiner.getParty(); 
    ...
  }

}
```

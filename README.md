# Spigot-CoreAPI #

## The Spigot side for the Bungee-Core-Plugin that communicates with Spigot plugins on game servers ##

This API provides methods for minigame plugins to handle player/party joining, server status updating, and a connection to MySQL. Some features include:

* Listening and handling player queues, allows/rejects a queue based on predicates defined by the minigame plugin
* Allows player to rejoin a game after a disconnection
* Allows the minigame plugin to handle parties
* Updates the game server and player queue status to communicate with the Bungee-Core-Plugin

## Commands ##
/spigotcmd join \<game\> - the spigot version of the bungee command /join \<game\> provided by the Bungee-Core-Plugin. Used for clickable NPC plugins.

## Dependencies ##

* [Party-API-For-Spigot](https://github.com/Simonsator/Party-API-For-Spigot)
* [Friends-API-For-Spigot-For-PAF-Proxy](https://github.com/Simonsator/Friends-API-For-Spigot-For-PAF-Proxy)

## Plugin Installation ##

0. Create and setup a Bungeecord network with a lobby and a game server. Create a MySQL database for plugin communication. Setup the Bungee-Core-Plugin along with the dependencies.
1. You will need to configure this plugin for both the lobby server and the game server. Place this plugin in the plugins folder and start the server. This will generate the plugin folder and you will be prompted to enter your database login information in mysql.yml.
2. In the settings.yml, enter your serverid for the lobby and game server (ex: "lobby," "oc001"). These ids should match the ones listed in the servers.yml of the Bungee-Core-Plugin.

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

### Handling player/parties joining and rejoining ###

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
  public void join(GameJoiner joiner) { //Called for every player that joins the lobby and upon rejoining the game
    PlayerParty party = joiner.getParty(); 
    ...
  }
}
```

### Updating server status ###

```java
API.getInstance().getStatusUpdater().setStarted(isServerStarted);
API.getInstance().getStatusUpdater().setWaiting(isAcceptingQueues);
```

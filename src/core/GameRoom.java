package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import server.Application;
import ui.Game;

public class GameRoom {
	String name;

	Map<String, User> users = new HashMap<>();
	User creator;
	int maxPlayers = 1;
	int id = 0;
	Game serverGame;
	public String chat ="";

	public GameRoom(User creator, String name, int max) {
		this.creator = creator;
		this.name = name;
		maxPlayers = max;
		serverGame = new Game();
		join(creator);
	}

	public GameRoom(String name, int max) {
		this.name = name;
		maxPlayers = max;
		serverGame = new Game();
	}

	public void join(User player) {
		if (users.containsKey(player.getUsername())) {

		} else {
			users.put(player.getUsername(), player);
			serverGame.addPlayer(player.getUsername());
		}
	}
	
	public void leave(String username) {
		User u = users.get(username);
		
		if(u != null) {
			users.remove(username);
			serverGame.removePlayer(username);
		}else {
			System.out.println(username+ " User doesn't exist");
		}

	}

	public void setGameId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<User> getUsers() {
		return new ArrayList<>(users.values());
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;

	}

	public void sendMsg(String username, String txt) {
		List<User> players = getUsers();
		for (User u : players) {
			Application serverApp = u.getConnection();
			if (serverApp != null)
				serverApp.sendMsg(txt, id, username);
		}

	}

	public void sendMsg(Message m) {
		List<User> players = getUsers();
		for (User u : players) {
			Application serverApp = u.getConnection();
			if (serverApp != null)
				serverApp.sendMsg(m);
		}

	}

	public void update(GameState m) {
		serverGame.setPlayers(m.getPlayers());
		serverGame.setTurn(m.getTurn());
	}
}

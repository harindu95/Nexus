package core;

import java.util.List;

public class Game {
	String name;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
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

	List<User> users;
	User creator;
	int maxPlayers = 1;
	int id = 0;
	
	public Game(User creator, String name, int max) {
		this.creator = creator;
		this.name = name;
		maxPlayers = max;
	}
	
	public Game(String name, int max) {
		this.name = name;
		maxPlayers = max;
	}
	
	public void setGameId(int id) {
		this.id = id;
	}
}


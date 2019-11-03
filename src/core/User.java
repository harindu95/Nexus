package core;

import server.Application;

public class User {
	private String username = "";
	private String password = "";
	int totalGames = 0;
	int wins = 0;
	server.Application serverApp;
		
	public User(String u, String p, int total, int win) {
		username = u;
		setPassword(p);
		totalGames = total;
		wins = win;
	}
	
	public User(String u) {
		username = u;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getTotalGames() {
		return totalGames;
	}

	public void setTotalGames(int totalGames) {
		this.totalGames = totalGames;
	}

	public int getWins() {
		return wins;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}

	public void setConnection(Application app) {
		serverApp = app;		
	}

	public Application getConnection() {
		return serverApp;
	}
	
}

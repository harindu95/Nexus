package server;

import java.util.ArrayList;
import java.util.List;

import core.Game;
import core.User;

public class GameList {
	List<Game> games;

	private static GameList instance = new GameList();

	private GameList() {
		games = new ArrayList<>();
	}

	public static GameList getInstance() {
		return instance;
	}

	public int createGame(User creator, String gameName, int max) {
		Game g = new Game(creator, gameName, max);
		int id = games.size();
		g.setId(id);
		games.add(g);
		return id;
	}
	
	public List<Game> getGames(){
		return games;
		
	}
	
	public Game joinGame(User player, int gameId) {
		Game g = games.get(gameId);
		g.join(player);
		return g;
	}
}

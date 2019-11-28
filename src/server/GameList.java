package server;

import java.util.ArrayList;
import java.util.List;

import core.GameRoom;
import core.User;

public class GameList {
	List<GameRoom> games;

	private static GameList instance = new GameList();

	private GameList() {
		games = new ArrayList<>();
	}

	public static GameList getInstance() {
		return instance;
	}

	public GameRoom createGame(User creator, String gameName, int max) {
		GameRoom g = new GameRoom(creator, gameName, max);
		int id = games.size();
		g.setId(id);
		games.add(g);
		return g;
	}
	
	public List<GameRoom> getGames(){
		return games;
		
	}
	
	public GameRoom joinGame(User player, int gameId) {
		GameRoom g = games.get(gameId);
		g.join(player);
		
		return g;
	}

	public GameRoom getGameRoom(int id) {
		if(id < games.size()) {
			return games.get(id);
		}
		return null;
	}

	public GameRoom observe(User player, int gameId) {
		GameRoom g = games.get(gameId);
		g.observe(player);
		
		return g;
	}
	
	
}

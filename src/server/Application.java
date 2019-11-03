package server;

import java.util.ArrayList;
import java.util.List;

import core.CreateGame_Reply;
import core.CreateGame_Request;
import core.Game;
import core.JoinGame_Reply;
import core.JoinGame_Request;
import core.Login_Reply;
import core.Login_Request;
import core.Message;
import core.OnlineUsers_Reply;
import core.OnlineUsers_Request;
import core.User;
import core.ViewGames_Reply;
import core.ViewGames_Request;

public class Application {

	List<User> currentUsers;
	UserDatabase users;
	Connection con;
	GameList games;

	Application(Connection con) {
		users = UserDatabase.getInstance();
		this.con = con;
		games = GameList.getInstance();
		games.createGame(users.getUser("test"), "test Game",32);
	}

	public void handle(Message msg) {
		if (msg instanceof Login_Request) {
			Login_Request req = (Login_Request) msg;
			User u = users.validate(req);
			System.out.println("Validating User");
			Login_Reply reply = new Login_Reply(u);
			con.writeMessage(reply);
		} else if (msg instanceof OnlineUsers_Request) {
			List<User> online = users.getOnlineUsers();
			OnlineUsers_Reply reply = new OnlineUsers_Reply(online);
			con.writeMessage(reply);
		} else if (msg instanceof CreateGame_Request) {
			CreateGame_Request req = (CreateGame_Request) msg;
			User u = users.getUser(req.getUserName());
			if (u == null) {
				// TODO: invalid user
			} else {
				int id = games.createGame(u, req.getGameName(), req.getMax());
				CreateGame_Reply reply = new CreateGame_Reply(id);
				con.writeMessage(reply);
			}
		} else if (msg instanceof ViewGames_Request) {
			ViewGames_Request req = (ViewGames_Request) msg;
			List<Game> gameList = games.getGames();
			ViewGames_Reply reply = new ViewGames_Reply(gameList);
			con.writeMessage(reply);
		} else if (msg instanceof JoinGame_Request) {
			JoinGame_Request req = (JoinGame_Request) msg;
			User player = users.getUser(req.getUsername());
			if (player == null) {
				System.out.println(req.getUsername());
				List<User> us = new ArrayList<>(users.users.values());
				for(User u: us) {
					System.out.println(u.getUsername()+"****");
				}
			
			} else {
				int gameId = req.getGameId();
				Game g = games.joinGame(player, gameId);
				// TODO: Check max players
				JoinGame_Reply reply = new JoinGame_Reply(g);
				con.writeMessage(reply);
			}
		}
	}

}

package server;

import java.util.ArrayList;
import java.util.List;

import core.ChatMessage;
import core.CreateGame_Request;
import core.GameRoom;
import core.GameState;
import core.JoinGame_Reply;
import core.JoinGame_Request;
import core.Login_Reply;
import core.Login_Request;
import core.Logout_Reply;
import core.Logout_Request;
import core.Message;
import core.OnlineUsers_Reply;
import core.OnlineUsers_Request;
import core.Reconnect_Reply;
import core.Reconnect_Request;
import core.RollDice;
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
		games.createGame(users.getUser("test"), "test Game", 32);
	}

	public void handle(Message msg) {
		if (msg instanceof Login_Request) {
			Login_Request req = (Login_Request) msg;
			User u = users.validate(req.getUserName(), req.getPassword());
			System.out.println("Validating User");
			if (u == null) {
				//TODO : invalid login
			} else {
				users.registerOnline(u, this);
				Login_Reply reply = new Login_Reply(u);
				con.send(reply);
			}
		} else if (msg instanceof OnlineUsers_Request) {
			List<User> online = users.getOnlineUsers();
			OnlineUsers_Reply reply = new OnlineUsers_Reply(online);
			con.send(reply);
		} else if (msg instanceof CreateGame_Request) {
			CreateGame_Request req = (CreateGame_Request) msg;
			User u = users.getUser(req.getUserName());
			if (u == null) {
				// TODO: invalid user
			} else {
				GameRoom game = games.createGame(u, req.getGameName(), req.getMax());
				JoinGame_Reply reply = new JoinGame_Reply(game);
				con.send(reply);
				GameState state = new GameState(game);
				game.sendMsg(state);
				
			}
		} else if (msg instanceof ViewGames_Request) {
			ViewGames_Request req = (ViewGames_Request) msg;
			List<GameRoom> gameList = games.getGames();
			ViewGames_Reply reply = new ViewGames_Reply(gameList);
			con.send(reply);
		} else if (msg instanceof JoinGame_Request) {
			JoinGame_Request req = (JoinGame_Request) msg;
			User player = users.getUser(req.getUsername());
			if (player == null) {
				// TODO: Error
				System.out.println("Invalid username");
			} else {
				int gameId = req.getGameId();
				GameRoom g = games.joinGame(player, gameId);
				// TODO: Check max players
				JoinGame_Reply reply = new JoinGame_Reply(g);
				con.send(reply);
				String txt = String.format("%s joined the game", player.getUsername());
				g.sendMsg(player.getUsername(), txt);
				GameState state = new GameState(g);
				g.sendMsg(state);

			}
		} else if (msg instanceof ChatMessage) {
			ChatMessage chatMsg = (ChatMessage) msg;
			GameRoom g = games.getGameRoom(chatMsg.getId());
			if (g == null) {
				// TODO: handle error
			} else {
				g.sendMsg(chatMsg.getUsername(), chatMsg.getMessage());
			}
		} else if (msg instanceof Logout_Request) {
			Logout_Request req = (Logout_Request) msg;
			User u = users.getUser(req.getUsername());
			if (u == null) {
				// TODO: error if user == null
			} else {
				users.logout(u);
				Logout_Reply reply = new Logout_Reply(u.getUsername());
				con.send(reply);
			}
		} else if (msg instanceof Reconnect_Request) {
			Reconnect_Request req = (Reconnect_Request) msg;
			User u = users.validate(req.getUsername(), req.getPassword());
			System.out.println("Validating User");
			if( u == null) {
				//TODO: INVALID user
			}else {
				Application app = u.getConnection();
				con.app = app;
				Reconnect_Reply reply = new Reconnect_Reply(u.getUsername());
				con.send(reply);
			}
		}else if(msg instanceof RollDice) {
			RollDice m = (RollDice) msg;
			games.getGameRoom(m.getGameId()).sendMsg(m);
		}
	}

	public void sendMsg(String msg, int gameId, String username) {
		ChatMessage chatMsg = new ChatMessage(msg, gameId, username);
		con.send(chatMsg);

	}

	public void sendMsg(Message m) {
		con.send(m);
		
	}

}

package server;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import core.ChatMessage;
import core.CreateGame_Request;
import core.GameRoom;
import core.GameState;
import core.JoinGame_Reply;
import core.JoinGame_Request;
import core.LeaveGame;
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

	}

	public void handle(Message msg) {
		if (msg instanceof Login_Request) {
			Login_Request req = (Login_Request) msg;
			User u = users.validate(req.getUserName(), req.getPassword());
			System.out.println("Validating User");
			if (u == null) {
				// TODO : invalid login
			} else {
				users.registerOnline(u, this);
				Login_Reply reply = new Login_Reply(u);
				con.send(reply);
				games.createGame(u, "test Game", 32);
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
			parseMessage(chatMsg);

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
			if (u == null) {
				// TODO: INVALID user
			} else {
				Application app = u.getConnection();
				con.app = app;
				Reconnect_Reply reply = new Reconnect_Reply(u.getUsername());
				con.send(reply);
			}
		} else if (msg instanceof RollDice) {
			RollDice m = (RollDice) msg;
			games.getGameRoom(m.getGameId()).sendMsg(m);
		} else if (msg instanceof GameState) {
			GameState m = (GameState) msg;
			System.out.println(m.toString());
			GameRoom room = games.getGameRoom(m.getGameId());
			room.update(m);
			room.sendMsg(new GameState(room));

		} else if (msg instanceof LeaveGame) {
			LeaveGame req = (LeaveGame) msg;
			GameRoom room = games.getGameRoom(req.getGameId());
			room.leave(req.getUsername());
			room.sendMsg(new GameState(room));
			String txt = String.format("%s left the game", req.getUsername());
			room.sendMsg(req.getUsername(), txt);

		}
	}

	private void parseMessage(ChatMessage chatMsg) {
		String text = chatMsg.getMessage();
		String regex = "@(\\w+)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(text);
		boolean publicMsg = true;
		while (matcher.find()) {
			publicMsg = false;
			String username = matcher.group(1);
			User u = users.getUser(username);
			System.out.println("Reciever name : " + username);
			if (u != null)
				u.getConnection().sendMsg(chatMsg);
			User sender = users.getUser(chatMsg.getUsername());
			sender.getConnection().sendMsg(chatMsg);
		}

		if (publicMsg) {
			GameRoom g = games.getGameRoom(chatMsg.getId());
			if (g == null) {
				// TODO: handle error
			} else {
				g.sendMsg(chatMsg.getUsername(), chatMsg.getMessage());
			}
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

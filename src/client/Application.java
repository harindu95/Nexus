package client;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
import core.ObserveGame_Reply;
import core.ObserveGame_Request;
import core.OnlineUsers_Reply;
import core.OnlineUsers_Request;
import core.Reconnect_Reply;
import core.Reconnect_Request;
import core.User;
import core.ViewGames_Reply;
import core.ViewGames_Request;
import javafx.application.Platform;
import javafx.stage.Stage;
import ui.Game;
import ui.GameLobby;
import ui.ListPlayers;
import ui.Main;
import ui.NetworkStatus;
import ui.UserMenu;
import ui.ViewGames;

public class Application {
	Connection con;
	User u;
	Main login;
	ListPlayers listPlayers;
	GameRoom currentGame;
	Stage mainStage;
	GameLobby lobby;
	NetworkStatus networkStatus;
	private ViewGames viewGames;
	private UserMenu userMenu;
	int retries = 0;
	Timer networkTimer;
	Game game;

	public Application() throws UnknownHostException, IOException {
		con = new Connection(this);
		Thread t = new Thread(con);
		t.start();
		networkStatus = new NetworkStatus(this);

	}

	public void handle(Message msg) {
		if (msg instanceof Login_Reply) {
			Login_Reply reply = (Login_Reply) msg;
//			System.out.println(reply.getUserName());
			loginReply(reply);
		} else if (msg instanceof OnlineUsers_Reply) {
			OnlineUsers_Reply reply = (OnlineUsers_Reply) msg;
			updateOnlineUsers(reply.getUsers());
		} else if (msg instanceof ViewGames_Reply) {
			ViewGames_Reply reply = (ViewGames_Reply) msg;
			updateViewGames(reply.getGames());
		} else if (msg instanceof JoinGame_Reply) {
			JoinGame_Reply reply = (JoinGame_Reply) msg;
			updateGameLobby(reply);
		} else if (msg instanceof ChatMessage) {
			ChatMessage chatMsg = (ChatMessage) msg;
			updateChat(chatMsg);
		} else if (msg instanceof Logout_Reply) {
			Logout_Reply reply = (Logout_Reply) msg;
			handleLogout(reply);
		} else if (msg instanceof Reconnect_Reply) {
			Reconnect_Reply reply = (Reconnect_Reply) msg;
			reconnected();
		} else if (msg instanceof GameState) {
			updateGame(msg);
		}else if(msg instanceof ObserveGame_Reply) {
			ObserveGame_Reply reply = (ObserveGame_Reply) msg;
			updateGameLobby(reply);
		}
	}

	private void updateGameLobby(ObserveGame_Reply reply) {
		currentGame = reply.getGame();
		lobby.update(currentGame);
		
	}

	private void updateGame(Message msg) {
		if (msg instanceof GameState) {
			GameState state = (GameState) msg;
//			System.out.println(state.toString());
			if (game == null || game.getId() != state.getGameId()) {
				game = new Game(this, state.getGameId(), u.getUsername());
			}
			game.setTurn(state.getTurn());
			game.update(state.getPlayers());
			lobby.updateUsers(state.getPlayers());
		}

	}

	private void reconnected() {

		retries = 0;
	}

	private void handleLogout(Logout_Reply reply) {

		if (reply.getUsername().equals(u.getUsername())) {
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					try {
						mainStage.close();
						login.show();

					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});
		}
	}

	private void updateChat(ChatMessage chatMsg) {
		if(currentGame == null) {
			
		}
		else if (chatMsg.getId() == currentGame.getId()) {
			String msg = String.format("%s: %s\n", chatMsg.getUsername(), chatMsg.getMessage());
			System.out.println(msg);
			currentGame.chat += msg;
			lobby.setChat(currentGame.chat);
			if (game != null)
				game.setChat(currentGame.chat);
		}

	}

	private void updateGameLobby(JoinGame_Reply reply) {
		currentGame = reply.getGame();
		lobby.update(currentGame);
	}

	private void updateViewGames(List<GameRoom> games) {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				viewGames.update(games);
			}
		});

	}

	private void showGameLobby() {

		lobby = new GameLobby(this);
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				lobby.start(mainStage);
//				lobby.update();
			}
		});

	}

	public GameRoom getCurrentGame() {
		return currentGame;
	}

	public void login(String username, String password) {
		Login_Request req = new Login_Request(username, password);
		con.send(req);
	}

	public void loginReply(Login_Reply reply) {
//		System.out.println(reply.getUserName());
		u = new User(reply.getUserName(), reply.getPassword(), reply.getTotal(), reply.getWins());
		showUserMenu(u.getUsername());
	}

	public void showUserMenu(String username) {

		userMenu = new UserMenu(this);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// javaFX operations should go here
				login.close();
				Stage window = new Stage();
				mainStage = window;
				userMenu.start(window);
			}
		});

		networkTimer = new Timer();
		TimerTask timerTask = new TimerTask() {

			@Override
			public void run() {
				int sent = con.getSentBytes();
				int recv = con.getRecvBytes();
				networkStatus.update(sent, recv);

			}
		};

		networkTimer.scheduleAtFixedRate(timerTask, 0l, 1000l);

	}

	public void showMainMenu() {

		Application app = this;
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// javaFX operations should go here
				userMenu.start(mainStage);
			}
		});

	}

	public void setLogin(Main login) {
		this.login = login;
	}

	public User getUser() {
		return u;
	}

	public void requestOnlineUsers() {
		OnlineUsers_Request req = new OnlineUsers_Request();
		con.send(req);
	}

	public void updateOnlineUsers(List<User> online) {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				listPlayers.update(online);
			}
		});
	}

	public void setListPlayers(ListPlayers controller) {
		listPlayers = controller;
	}

	public void createGame(String gameName, int maxPlayers) {
		CreateGame_Request req = new CreateGame_Request(u.getUsername(), gameName, maxPlayers);
		currentGame = new GameRoom(u, gameName, maxPlayers);
		con.send(req);
		showGameLobby();
	}

	public void viewGames(ViewGames viewGames) {
		this.viewGames = viewGames;
		ViewGames_Request req = new ViewGames_Request();
		con.send(req);
	}

	public void joinGame(int id) {

		JoinGame_Request req = new JoinGame_Request(u.getUsername(), id);
		con.send(req);
		showGameLobby();
	}

	public void sendMsg(String msg) {
		ChatMessage chatMsg = new ChatMessage(msg, currentGame.getId(), u.getUsername());
		con.send(chatMsg);

	}

	public void logout() {
		Logout_Request req = new Logout_Request(u.getUsername());
		con.send(req);
	}

	public void reconnect() throws IOException, InterruptedException {
		retries++;
		Thread.sleep(300);
		if (retries > 30) {
			throw new IOException("Disconnected from server. Failed to restablish connection");
		}
		try {
			con.reset();
			Reconnect_Request req = new Reconnect_Request(u.getUsername(), u.getPassword());
			con.send(req);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void showNetworkStatus() {

		networkStatus.start(mainStage);

	}

	public void showGame() {
//		game = new Game(this, currentGame.getId(), u.getUsername());
		game.setChat(lobby.getChat());
		game.start(mainStage);

	}
	

	public void gameChanged() {
		GameState msg = GameState.fromGame(game);
		con.send(msg);

	}
	
	
	public void leaveGame() {
		LeaveGame msg = new LeaveGame(u.getUsername(), currentGame.getId());
		con.send(msg);
		currentGame = null;
		showMainMenu();
	}

	public void observeGame(int id) {
		ObserveGame_Request req = new ObserveGame_Request(u.getUsername(), id);
		con.send(req);
		showGameLobby();
	}
}

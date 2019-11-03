package client;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import core.CreateGame_Reply;
import core.CreateGame_Request;
import core.Game;
import core.Login_Reply;
import core.Login_Request;
import core.Message;
import core.OnlineUsers_Reply;
import core.OnlineUsers_Request;
import core.User;
import core.ViewGames_Reply;
import core.ViewGames_Request;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import ui.GameLobby;
import ui.ListPlayers;
import ui.UserMenu;
import ui.ViewGames;

public class Application {
	Client c;
	User u;
	Stage loginStage;
	ListPlayers listPlayers;
	Game currentGame;
	Stage mainStage;
	GameLobby lobby;
	private ViewGames viewGames;
	private UserMenu userMenu;

	public Application() throws UnknownHostException, IOException {
		c = new Client(this);
		Thread t = new Thread(c);
		t.start();
	}

	public void handle(Message msg) {
		if (msg instanceof Login_Reply) {
			Login_Reply reply = (Login_Reply) msg;
//			System.out.println(reply.getUserName());
			loginReply(reply);
		} else if (msg instanceof OnlineUsers_Reply) {
			OnlineUsers_Reply reply = (OnlineUsers_Reply) msg;
			updateOnlineUsers(reply.getUsers());
		} else if (msg instanceof CreateGame_Reply) {
			CreateGame_Reply reply = (CreateGame_Reply) msg;
			setGame(reply);
		}else if(msg instanceof ViewGames_Reply) {
			ViewGames_Reply reply = (ViewGames_Reply) msg;
			updateViewGames(reply.getGames());
		}
	}

	private void updateViewGames(List<Game> games) {
		
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				viewGames.update(games);		
			}
		});
		
	}

	private void setGame(CreateGame_Reply reply) {
		currentGame.setGameId(reply.getGameId());
		showGameLobby();
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

	public Game getCurrentGame() {
		return currentGame;
	}

	public void login(String username, String password) {
		Login_Request req = new Login_Request(username, password);
		c.setOutput(req);
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
				loginStage.close();
				Stage window = new Stage();
				mainStage = window;
				userMenu.start(window);
			}
		});

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
	
	public void setLoginStage(Stage login) {
		this.loginStage = login;
	}

	public User getUser() {
		return u;
	}

	public void requestOnlineUsers() {
		OnlineUsers_Request req = new OnlineUsers_Request();
		c.setOutput(req);
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
		currentGame = new Game(u, gameName, maxPlayers);
		c.setOutput(req);
	}

	public void viewGames(ViewGames viewGames) {
		this.viewGames = viewGames;
		ViewGames_Request req = new ViewGames_Request();
		c.setOutput(req);
	}

	public void joinGame(int id) {
		// TODO Auto-generated method stub
		JoinGame_Request req = new JoinGame_Request(u.getUsername(), id);
	}

}

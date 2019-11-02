package client;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import core.Login_Reply;
import core.Login_Request;
import core.Message;
import core.OnlineUsers_Reply;
import core.OnlineUsers_Request;
import core.User;
import javafx.application.Platform;
import javafx.stage.Stage;
import ui.ListPlayers;
import ui.UserMenu;

public class Application {
	Client c;
	User u;
	Stage loginStage;
	ListPlayers listPlayers;
	
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
		}else if(msg instanceof OnlineUsers_Reply) {
			OnlineUsers_Reply reply = (OnlineUsers_Reply) msg;
			updateOnlineUsers(reply.getUsers());
		}
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
		
		Application app = this;
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// javaFX operations should go here
	        	loginStage.close();
				Stage window = new Stage();
				UserMenu userMenu = new UserMenu();
				userMenu.start(window, app);
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

}

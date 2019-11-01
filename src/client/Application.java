package client;

import java.io.IOException;
import java.net.UnknownHostException;

import core.Login_Reply;
import core.Login_Request;
import core.Message;
import core.User;
import javafx.application.Platform;
import javafx.stage.Stage;
import ui.UserMenu;

public class Application {
	Client c;
	User u;

	public Application() throws UnknownHostException, IOException {
		c = new Client(this);
		Thread t = new Thread(c);
		t.start();
	}

	public void handle(Message msg) {
		if (msg instanceof Login_Reply) {
			Login_Reply reply = (Login_Reply) msg;
			System.out.println(reply.getUserName());
			loginReply(reply);
		}
	}

	public void login(String username, String password) {
		Login_Request req = new Login_Request(username, password);
		c.setOutput(req);
	}

	public void loginReply(Login_Reply reply) {
		System.out.println(reply.getUserName());
		u = new User(reply.getUserName(), reply.getPassword(), reply.getTotal(), reply.getWins());
		showUserMenu(u.getUsername());
	}

	public void showUserMenu(String username) {
		
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// javaFX operations should go here
//	        	login.close();
				Stage window = new Stage();
				UserMenu userMenu = new UserMenu();
				userMenu.start(window, username);
			}
		});
		
	}

}

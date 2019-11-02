package server;

import java.util.List;

import core.CreateGame_Reply;
import core.CreateGame_Request;
import core.Login_Reply;
import core.Login_Request;
import core.Message;
import core.OnlineUsers_Reply;
import core.OnlineUsers_Request;
import core.User;

public class Application {
	
	List<User> currentUsers;
	UserDatabase users;
	Connection con;
	GameList games;
	
	Application(Connection con){
		users = UserDatabase.getInstance();
		this.con = con;
		games = GameList.getInstance();
	}
	
	public void handle(Message msg) {
		if( msg instanceof Login_Request) {
			Login_Request req = (Login_Request) msg;
			User u = users.validate(req);
			System.out.println("Validating User");
			Login_Reply reply = new Login_Reply(u);
			con.writeMessage(reply);
		}else if(msg instanceof OnlineUsers_Request) {
			List<User> online = users.getOnlineUsers();
			OnlineUsers_Reply reply = new OnlineUsers_Reply(online);
			con.writeMessage(reply);
		}else if(msg instanceof CreateGame_Request) {
			CreateGame_Request req = (CreateGame_Request)msg;
			User u = users.getUser(req.getUserName());
			if(u == null) {
				//TODO: invalid user
			}else {
				int id = games.createGame(u, req.getGameName(), req.getMax());
				CreateGame_Reply reply = new CreateGame_Reply(id);
				con.writeMessage(reply);
			}
		}
	}
	
}

package server;

import java.util.List;

import core.Login_Reply;
import core.Login_Request;
import core.Message;
import core.User;

public class Application {
	
	List<User> currentUsers;
	UserDatabase users;
	Server s;
	
	Application(Server server){
		users = new UserDatabase();
		s = server;
	}
	
	public void handle(Message msg) {
		if( msg instanceof Login_Request) {
			Login_Request req = (Login_Request) msg;
			User u = users.validate(req);
			System.out.println("Validating User");
			Login_Reply reply = new Login_Reply(u);
			s.writeMessage(reply);
		}
	}
	
}

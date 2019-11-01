package server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.Login_Request;
import core.User;

public class UserDatabase {
	Map<String, User> users;

	UserDatabase() {
		users = new HashMap<String, User>();
		users.put("test", new User("test", "test", 0, 0));
	}

	User validate(Login_Request req) {
		users.put("test", new User("test", "test", 0, 0));
		String password = req.getPassword();
		String username = req.getUserName();
		
		users.put(username, new User(username, password, 0,0));
		User u = users.get(req.getUserName());
		if (u != null) {

			if (u.getPassword().equals(req.getPassword())) {
				return u;
			}

		}
		System.out.println("No record found!");
		return null;

	}

}

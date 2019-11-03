package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.Login_Request;
import core.User;

public class UserDatabase {
	Map<String, User> users;
	Map<String, User> online;
	
	static UserDatabase instance = null;
	
	private UserDatabase() {
		users = new HashMap<String, User>();
		users.put("test", new User("test", "test", 0, 0));
		online = new HashMap<>();
	}

	public void registerOnline(User u,Application app) {
		online.put(u.getUsername(), u);
		u.setConnection(app);
	}
	
	public void logout(User u) {
		online.remove(u.getUsername());
	}
	
	User validate(String username, String password) {
		users.put("test", new User("test", "test", 0, 0));
	
		users.put(username, new User(username, password, 0,0));
		User u = users.get(username);
		if (u != null) {

			if (u.getPassword().equals(password)) {
				
				return u;
			}

		}
		System.out.println("No record found!");
		return null;

	}
	
	List<User> getOnlineUsers(){
		return new ArrayList<>(online.values());
	}

	User getUser(String username) {
		return users.get(username);
	}
	
	public static UserDatabase getInstance() {
		if(instance == null)
			instance = new UserDatabase();
		return instance;
	}
}

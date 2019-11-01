package server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.Login_Request;
import core.User;

public class UserDatabase {
	Map<String, User> users;
	
	UserDatabase(){
		users = new HashMap<String, User>();
		users.put("test", new User("test", "test", 0,0));
	}
	
	User validate(Login_Request req) {
		
		if(users.containsKey(req.getUserName())) {
			User u =  users.get(req.getUserName());
			if(u.getPassword().equals(req.getPassword())) {
				return u;
			}
		}
		
		return null;
		
	}
	
	
}

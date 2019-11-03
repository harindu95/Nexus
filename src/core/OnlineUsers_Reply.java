package core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class OnlineUsers_Reply extends Message{

	List<User> online;
	
	private OnlineUsers_Reply() {
		super(Type.ONLINEUSERS_REPLY);
		this.online = new ArrayList<>();
	}
	
	public OnlineUsers_Reply(List<User> online){
		super(Type.ONLINEUSERS_REPLY);
		this.online = online;
	}

	@Override
	public void write(ByteArrayOutputStream os) throws IOException {
		
		byte numUsers = (byte) online.size();
		os.write(numUsers);
		for(int i=0;i<numUsers;i++) {
			User u = online.get(i);
			os.write(Util.strByteArray(u.getUsername(),16));
		}
	
	}
	
	public static OnlineUsers_Reply read(ByteArrayInputStream is) throws IOException {
		byte numUsers = (byte)is.read();
		byte[] username = new byte[USERNAME_SIZE];
		OnlineUsers_Reply reply = new OnlineUsers_Reply();
		for(int i=0;i<numUsers;i++) {
			is.read(username);
			User u = new User( Util.toString(username));
			reply.online.add(u);
		}
		return reply;
	}

	public List<User> getUsers() {
	
		return online;
	}
}


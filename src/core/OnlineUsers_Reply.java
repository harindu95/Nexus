package core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class OnlineUsers_Reply implements Message{

	Message.Type type = Message.Type.ONLINEUSERS_REPLY;
	List<User> online;
	
	private OnlineUsers_Reply() {
		this.online = new ArrayList<>();
	}
	
	public OnlineUsers_Reply(List<User> online){
		this.online = online;
	}

	@Override
	public void write(OutputStream os) throws IOException {
		byte size = (byte) (16 * online.size());
		os.write(size);
		byte type = (byte) this.type.ordinal();
		os.write(type);
		byte numUsers = (byte) online.size();
		os.write(numUsers);
		for(int i=0;i<numUsers;i++) {
			User u = online.get(i);
			os.write(Util.strByteArray(u.getUsername(),16));
		}
		System.out.println("Message sent : OnlineUsers_Reply");
	}
	
	public static OnlineUsers_Reply read(InputStream is) throws IOException {
		byte numUsers = (byte)is.read();
		byte[] username = new byte[16];
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


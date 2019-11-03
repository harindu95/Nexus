package core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import core.Message.Type;

public class OnlineUsers_Request extends Message {

	
	public OnlineUsers_Request() {
		super(Type.ONLINEUSERS_REQUEST);
	}
	
	@Override
	public void write(ByteArrayOutputStream os) throws IOException {
		byte size = 0;
		byte[] payload = {size, type};
		os.write(payload);
		System.out.println("Message sent : OnlineUsers_Request");
	}

}

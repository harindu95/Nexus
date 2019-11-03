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
	
		
	}

}

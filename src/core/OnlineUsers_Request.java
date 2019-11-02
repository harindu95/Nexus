package core;

import java.io.IOException;
import java.io.OutputStream;

public class OnlineUsers_Request implements Message {

	Message.Type type = Message.Type.ONLINEUSERS_REQUEST;
	
	@Override
	public void write(OutputStream os) throws IOException {
		byte size = 2;
		byte type = (byte)this.type.ordinal();
		byte[] payload = {size, type};
		os.write(payload);
		System.out.println("Message sent : OnlineUsers_Request");
	}

}

package core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Message {
	enum Type {
		EMPTY,
		LOGIN_REQUEST,LOGIN_REPLY, 
		ONLINEUSERS_REQUEST, ONLINEUSERS_REPLY,
		CREATEGAME_REQUEST, CREATEGAME_REPLY,
		VIEWGAMES_REQUEST, VIEWGAMES_REPLY,
		JOINGAME_REQUEST, JOINGAME_REPLY,
		CHATMESSAGE,
	}
	public void write(OutputStream os) throws IOException;
			
}

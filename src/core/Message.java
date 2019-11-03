package core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class Message {
	public enum Type {
		EMPTY,
		LOGIN_REQUEST,LOGIN_REPLY, 
		ONLINEUSERS_REQUEST, ONLINEUSERS_REPLY,
		CREATEGAME_REQUEST, CREATEGAME_REPLY,
		VIEWGAMES_REQUEST, VIEWGAMES_REPLY,
		JOINGAME_REQUEST, JOINGAME_REPLY,
		CHATMESSAGE,
		LOGOUT_REQUEST, LOGOUT_REPLY,
		RECONNECT_REQUEST, RECONNECT_REPLY
	}
	
	final int USERNAME_SIZE = 16;
	final int PASSWORD_SIZE = 25;
	final int HEADER_SIZE = 2;
	
	byte type = 0;
	
	Message(Type t){
		type = (byte) t.ordinal();
	}
	
	public abstract void write(OutputStream os) throws IOException;
	
	public String toString() {
		Type t = Type.values()[type];
		return t.toString();
	}
}

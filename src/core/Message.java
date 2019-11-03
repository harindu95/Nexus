package core;

import java.io.ByteArrayOutputStream;
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
	
		LOGOUT_REQUEST, LOGOUT_REPLY,
		RECONNECT_REQUEST, RECONNECT_REPLY,
		CHATMESSAGE,
	}
	
	static final int USERNAME_SIZE = 16;
	static final int PASSWORD_SIZE = 25;
//	static final int HEADER_SIZE = 2;
	static final int GAMENAME_SIZE = 30;
	static final int MAXPLAYERS_SIZE = 1;
	static final int GAMEID_SIZE = 1;
	static final int STATUS = 1;
	static final int TOTALGAMES_SIZE = 1;
	static final int WINS_SIZE = 1;
	static final int CHATMESSAGE_SIZE = 200;
	static final int NUMGAMES_SIZE = 1;
	static final int NUMPLAYERS_SIZE = 1;
	
	byte type = 0;
		
	Message(Type t){
		type = (byte) t.ordinal();
	}
	
	public abstract void write(ByteArrayOutputStream os) throws IOException;
	
	public String toString() {
		Type t = Type.values()[type];
		return t.toString();
	}
		
	public byte getType() {
		return type;
	}
}

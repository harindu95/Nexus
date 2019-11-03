package core;

import java.io.IOException;
import java.io.OutputStream;

public class JoinGame_Request implements Message{

	byte gameId;
	byte[] username = new byte[30];
	byte type = (byte)Type.JOINGAME_REQUEST.ordinal();
	
	public JoinGame_Request(String username, int gameId) {
		this.gameId = (byte) gameId;
		Util.strncpy(this.username, username);
	}
	
	public void write(OutputStream os) throws IOException {
//		byte size = 
	}
	
	
}

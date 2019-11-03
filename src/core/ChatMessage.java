package core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ChatMessage extends Message{
	byte[] username = new byte[USERNAME_SIZE];
	byte gameId = 0;
	byte[] message = new byte[CHATMESSAGE_SIZE];
		
	private ChatMessage() {
		super(Type.CHATMESSAGE);
	}
	
	public ChatMessage(String txt, int gameId, String username) {
		super(Type.CHATMESSAGE);
		Util.strncpy(this.username, username);
		Util.strncpy(message, txt);
		this.gameId = (byte) gameId;
	}
	
	@Override
	public void write(OutputStream os) throws IOException {
		byte size = (byte) (GAMEID_SIZE + USERNAME_SIZE +CHATMESSAGE_SIZE);
		byte[] header = {size, type, gameId};
		os.write(header);
		os.write(username);
		os.write(message);
		
	}
	
	public static ChatMessage read(InputStream is) throws IOException {
		ChatMessage msg = new ChatMessage();
		byte[] header = new byte[GAMEID_SIZE];
		is.read(header);
		msg.gameId = header[0];
		is.read(msg.username);
		is.read(msg.message);
		return msg;
	}
	
	public String getUsername() {
		return Util.toString(username);
	}
	
	public String getMessage() {
		return Util.toString(message);
	}

	public int getId() {
		return gameId;
	}
}

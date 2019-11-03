package core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ChatMessage implements Message{
	byte[] username = new byte[16];
	byte gameId = 0;
	byte[] message = new byte[200];
	byte type = (byte)Message.Type.CHATMESSAGE.ordinal();
	
	private ChatMessage() {
		
	}
	
	public ChatMessage(String txt, int gameId, String username) {
		Util.strncpy(this.username, username);
		Util.strncpy(message, txt);
		this.gameId = (byte) gameId;
	}
	
	@Override
	public void write(OutputStream os) throws IOException {
		byte size = (byte) (200 + 16 + 1 + 2);
		byte[] header = {size, type, gameId};
		os.write(header);
		os.write(username);
		os.write(message);
		
	}
	
	public static ChatMessage read(InputStream is) throws IOException {
		ChatMessage msg = new ChatMessage();
		byte[] header = new byte[1];
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

package core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import core.Message.Type;

public class JoinGame_Request extends Message {

	byte gameId;
	byte[] username = new byte[30];

	private JoinGame_Request() {
		super(Type.JOINGAME_REQUEST);
	}
	
	public JoinGame_Request(String username, int gameId) {
		super(Type.JOINGAME_REQUEST);
		this.gameId = (byte) gameId;
		Util.strncpy(this.username, username);
	}

	public void write(OutputStream os) throws IOException {
		byte size = 30 + 1 + 1 + 1;
		byte[] header = { size, type, gameId };
		os.write(header);
		os.write(username);
	}
	
	public static JoinGame_Request read(InputStream is) throws IOException {
		JoinGame_Request req = new JoinGame_Request();
		byte[] header = new byte[1];
		is.read(header);
		req.gameId = header[0];
		is.read(req.username);
		return req;
	}
	
	public String getUsername() {
		return  Util.toString(username);
	}
	
	public int getGameId() {
		return gameId;
	}

}

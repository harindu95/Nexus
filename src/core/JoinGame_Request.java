package core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class JoinGame_Request extends Message {

	byte gameId;
	byte[] username = new byte[USERNAME_SIZE];

	private JoinGame_Request() {
		super(Type.JOINGAME_REQUEST);
	}
	
	public JoinGame_Request(String username, int gameId) {
		super(Type.JOINGAME_REQUEST);
		this.gameId = (byte) gameId;
		Util.strncpy(this.username, username);
	}

	public void write(ByteArrayOutputStream os) throws IOException {
		byte[] header = {  gameId };
		os.write(header);
		os.write(username);
	
	}
	
	public static JoinGame_Request read(ByteArrayInputStream is) throws IOException {
		JoinGame_Request req = new JoinGame_Request();
		byte[] header = new byte[GAMEID_SIZE];
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

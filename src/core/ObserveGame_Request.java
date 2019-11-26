package core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ObserveGame_Request extends Message {

	byte gameId;
	byte[] username = new byte[USERNAME_SIZE];

	private ObserveGame_Request() {
		super(Type.OBSERVEGAME_REQUEST);
	}
	
	public ObserveGame_Request(String username, int gameId) {
		super(Type.OBSERVEGAME_REQUEST);
		this.gameId = (byte) gameId;
		Util.strncpy(this.username, username);
	}

	public void write(ByteArrayOutputStream os) throws IOException {
		byte[] header = {  gameId };
		os.write(header);
		os.write(username);
	
	}
	
	public static ObserveGame_Request read(ByteArrayInputStream is) throws IOException {
		ObserveGame_Request req = new ObserveGame_Request();
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

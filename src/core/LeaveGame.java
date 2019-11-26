package core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class LeaveGame extends Message{

	byte gameId;
	byte[] username = new byte[USERNAME_SIZE];
	
	private LeaveGame() {
		super(Message.Type.LEAVEGAME);
	}
	
	public LeaveGame(String username, int gameId) {
		super(Message.Type.LEAVEGAME);
		this.gameId = (byte) gameId;
		Util.strncpy(this.username, username);
	}

	@Override
	public void write(ByteArrayOutputStream os) throws IOException {
		byte[] header = {gameId};
		os.write(header);
		os.write(username);		
	}
	
	public static LeaveGame read(ByteArrayInputStream is) throws IOException {
		LeaveGame req = new LeaveGame();
		req.gameId = (byte)is.read();
		is.read(req.username);
		return req;
	}

	public int getGameId() {
		
		return gameId;
	}
	
	public String getUsername() {
		return Util.toString(username);
	}
	
}

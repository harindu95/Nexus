package core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import core.Message.Type;

public class RollDice extends Message{

	byte gameId;
	byte[] username = new byte[USERNAME_SIZE];
	byte dice = 1;

	private RollDice() {
		super(Type.ROLLDICE);
	}
	
	public RollDice(String username, int gameId, int dice) {
		super(Type.ROLLDICE);
		this.gameId = (byte) gameId;
		this.dice = (byte) dice;
		Util.strncpy(this.username, username);
	}

	public void write(ByteArrayOutputStream os) throws IOException {
		byte[] header = { gameId,dice};
		os.write(header);
		os.write(username);
	
	}
	
	public static RollDice read(ByteArrayInputStream is) throws IOException {
		RollDice req = new RollDice();
		byte[] header = new byte[2];
		is.read(header);
		req.gameId = header[0];
		req.dice = header[1];
		is.read(req.username);
		return req;
	}
		
	public String getUsername() {
		return  Util.toString(username);
	}
	
	public int getGameId() {
		return gameId;
	}
	
	public int getDice() {
		return dice % 7 ;
	}

}

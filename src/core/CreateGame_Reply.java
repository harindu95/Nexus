package core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CreateGame_Reply implements Message {

	byte gameId;
	byte status = 0;
	byte type = (byte)Message.Type.CREATEGAME_REPLY.ordinal();
	
	public CreateGame_Reply(int gameId) {
		this.gameId = (byte) gameId;
	}

	@Override
	public void write(OutputStream os) throws IOException {
		byte size = 4;
		byte[] payload = { size, type, status, gameId};
		os.write(payload);
		System.out.println("Message sent : CreateGame_Reply");
	}
	
	public static CreateGame_Reply read(InputStream is) throws IOException{
		byte[] payload = new byte[2];
		is.read(payload);
		CreateGame_Reply reply = new CreateGame_Reply(payload[1]);
		reply.status = payload[0];
		return reply;
	}

	public byte getGameId() {
		return gameId;
	}

	public void setGameId(byte gameId) {
		this.gameId = gameId;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}
	
	
}

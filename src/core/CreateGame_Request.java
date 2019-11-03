package core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import core.Message.Type;

public class CreateGame_Request extends Message {

//	Size(first byte) = 1 byte
//			Message type = 1 byte
//			Char[16] Username = 16 bytes
//			Char[30] gameName = 30 bytes

	byte[] username = new byte[USERNAME_SIZE];
	byte[] gamename = new byte[GAMENAME_SIZE];
	byte max = 0;
	
	
	public CreateGame_Request(String username, String gameName, int max) {
		super(Type.CREATEGAME_REQUEST);
		this.max = (byte) max;
		Util.strncpy(this.username,username);
		Util.strncpy(gamename, gameName);
	}

	private CreateGame_Request() {
		super(Type.CREATEGAME_REQUEST);
	}

	@Override
	public void write(OutputStream os) throws IOException {
		byte size = GAMENAME_SIZE + USERNAME_SIZE + MAXPLAYERS_SIZE;
		byte[] payload = {size, type};
		os.write(payload);
		os.write(username);
		os.write(gamename);
		byte[] p = { max};
		os.write(p);
		
	}
	

	public static CreateGame_Request read(InputStream is) throws IOException {
		CreateGame_Request req = new CreateGame_Request();
		is.read(req.username);
		is.read(req.gamename);
		byte[] s = new byte[MAXPLAYERS_SIZE];
		is.read(s);
		req.max = s[0];
		return req;
	}
	
	
	public String getUserName() {
		return Util.toString(username);
	}
	
	public String getGameName() {
		return  Util.toString(gamename);
	}
	
	public int getMax() {
		return max;
	}
	
}

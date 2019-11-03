package core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Logout_Request implements Message{
	byte[] username = new byte[16];
	byte type = (byte)Type.LOGOUT_REQUEST.ordinal();

	private Logout_Request() {
	}
	
	public Logout_Request(String username) {
		Util.strncpy(this.username, username);
	}
	
	@Override
	public void write(OutputStream os) throws IOException {
		byte size = 16 + 2;
		byte[] header = { size, type};
		os.write(header);
		os.write(username);		
	}
	
	public static Logout_Request read(InputStream is) throws IOException {
		Logout_Request req = new Logout_Request();
		is.read(req.username);
		return req;
	}
	
	public String getUsername() {
		return Util.toString(username);
	
	}
}

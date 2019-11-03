package core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import core.Message.Type;

public class Logout_Request extends Message{
	byte[] username = new byte[USERNAME_SIZE];
	
	private Logout_Request() {
		super(Type.LOGOUT_REQUEST);
	}
	
	public Logout_Request(String username) {
		super(Type.LOGOUT_REQUEST);
		Util.strncpy(this.username, username);
	}
	
	@Override
	public void write(ByteArrayOutputStream os) throws IOException {
		byte size = USERNAME_SIZE;
		byte[] header = { size, type};
		os.write(header);
		os.write(username);		
	}
	
	public static Logout_Request read(ByteArrayInputStream is) throws IOException {
		Logout_Request req = new Logout_Request();
		is.read(req.username);
		return req;
	}
	
	public String getUsername() {
		return Util.toString(username);
	
	}
}

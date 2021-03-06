package core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Reconnect_Request extends Message{

	byte[] username = new byte[USERNAME_SIZE];
	byte[] password = new byte[PASSWORD_SIZE];
	
	private Reconnect_Request() {
		super(Type.RECONNECT_REQUEST);
	}
	
	public Reconnect_Request(String username, String password) {
		super(Type.RECONNECT_REQUEST);
		Util.strncpy(this.username, username);
		Util.strncpy(this.password, password);
	}

	@Override
	public void write(ByteArrayOutputStream os) throws IOException {
		
		os.write(username);
		os.write(password);
	}
	
	public static Reconnect_Request read(ByteArrayInputStream is) throws IOException{
		Reconnect_Request req = new Reconnect_Request();
		is.read(req.username);
		is.read(req.password);
		return req;
	}
	
	public String getUsername() {
		return Util.toString(username);
	}
	
	public String getPassword() {
		return Util.toString(password);
	}

}

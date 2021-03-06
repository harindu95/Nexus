package core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Logout_Reply extends Message {
	byte[] username = new byte[USERNAME_SIZE];
	
	private Logout_Reply() {
		super(Type.LOGOUT_REPLY);
	}

	public Logout_Reply(String username) {
		super(Type.LOGOUT_REPLY);
		Util.strncpy(this.username, username);
	}

	@Override
	public void write(ByteArrayOutputStream os) throws IOException {
		
		os.write(username);
	}

	public static Logout_Reply read(ByteArrayInputStream is) throws IOException {
		Logout_Reply req = new Logout_Reply();
		is.read(req.username);
		return req;
	}

	public String getUsername() {
		return Util.toString(username);

	}
}

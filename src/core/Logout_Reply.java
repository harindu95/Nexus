package core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import core.Message.Type;

public class Logout_Reply extends Message {
	byte[] username = new byte[16];
	
	private Logout_Reply() {
		super(Type.LOGOUT_REPLY);
	}

	public Logout_Reply(String username) {
		super(Type.LOGOUT_REPLY);
		Util.strncpy(this.username, username);
	}

	@Override
	public void write(OutputStream os) throws IOException {
		byte size = 16 + 2;
		byte[] header = { size, type };
		os.write(header);
		os.write(username);
	}

	public static Logout_Reply read(InputStream is) throws IOException {
		Logout_Reply req = new Logout_Reply();
		is.read(req.username);
		return req;
	}

	public String getUsername() {
		return Util.toString(username);

	}
}

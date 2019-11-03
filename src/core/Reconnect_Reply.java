package core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Reconnect_Reply extends Message {

	byte[] username = new byte[USERNAME_SIZE];

	private Reconnect_Reply() {
		super(Type.RECONNECT_REPLY);
	}

	public Reconnect_Reply(String username) {
		super(Type.RECONNECT_REPLY);
		Util.strncpy(this.username, username);

	}

	@Override
	public void write(ByteArrayOutputStream os) throws IOException {
		byte size = USERNAME_SIZE;
		byte[] header = { size, type };
		os.write(header);
		os.write(username);

	}

	public static Reconnect_Reply read(ByteArrayInputStream is) throws IOException {
		Reconnect_Reply req = new Reconnect_Reply();
		is.read(req.username);
		return req;
	}

	public String getUsername() {
		return Util.toString(username);
	}

}

package core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Login_Reply implements Message {

	byte[] username = new byte[16];
	byte[] password = new byte[25];
	byte type = (byte) Message.Type.LOGIN_REPLY.ordinal();
	byte total = 0;
	byte wins = 0;
	byte status = 0;

	private Login_Reply() {

	}

	public Login_Reply(User u) {
		if (u == null) {
			
		} else {
			Util.strncpy(this.username, u.getUsername());
			Util.strncpy(this.password, u.getPassword());
			wins = (byte)u.getWins();
			total = (byte)u.getTotalGames();
		}
	}

	public void write(OutputStream os) throws IOException {
		int size = 43;
		byte[] payload = { 43, type, status };
		os.write(payload);
		os.write(username);
		os.write(password);
		byte[] p = {total, wins};
		os.write(p);
	}

	public static Login_Reply read(InputStream is) throws IOException {
		Login_Reply reply = new Login_Reply();
		byte[] status = new byte[1];
		is.read(status);
		reply.status = status[0];
		is.read(reply.username);
		is.read(reply.password);
		byte[] p = new byte[2];
		is.read(p);
		reply.total = p[0];
		reply.wins = p[1];
		return reply;
	}

	public String getUserName() {
		return new String(username);
	}

	public String getPassword() {
		return new String(password);
	}
}

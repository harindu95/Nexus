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

	enum error {
		SUCCESS,
		INVALID
	}
	
	private Login_Reply() {

	}

	public byte[] getUsername() {
		return username;
	}

	public void setUsername(byte[] username) {
		this.username = username;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public byte getTotal() {
		return total;
	}

	public void setTotal(byte total) {
		this.total = total;
	}

	public byte getWins() {
		return wins;
	}

	public void setWins(byte wins) {
		this.wins = wins;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public void setPassword(byte[] password) {
		this.password = password;
	}

	public Login_Reply(User u) {
		if (u == null) {
			this.status = (byte)error.INVALID.ordinal(); 
		} else {
			System.out.println("username :" + u.getUsername());
			Util.strncpy(this.username, u.getUsername());
			Util.strncpy(this.password, u.getPassword());
			wins = (byte) u.getWins();
			total = (byte) u.getTotalGames();
		}
	}

	public void write(OutputStream os) throws IOException {
		int size = 43;
		byte[] payload = { 43, type, status };
		os.write(payload);
		os.write(username);
		os.write(password);
		byte[] p = { total, wins };
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

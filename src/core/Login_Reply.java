package core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Login_Reply extends Message {

	byte[] username = new byte[USERNAME_SIZE];
	byte[] password = new byte[PASSWORD_SIZE];
	byte total = 0;
	byte wins = 0;
	byte status = 0;

	enum error {
		SUCCESS,
		INVALID
	}
	
	private Login_Reply() {
		super(Type.LOGIN_REPLY);
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
		super(Type.LOGIN_REPLY);
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

	public void write(ByteArrayOutputStream os) throws IOException {
		
		byte[] payload = { status };
		os.write(payload);
		os.write(username);
		os.write(password);
		byte[] p = { total, wins };
		os.write(p);
	}

	public static Login_Reply read(ByteArrayInputStream is) throws IOException {
		Login_Reply reply = new Login_Reply();
		byte[] status = new byte[STATUS];
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
		return  Util.toString(username);
	}

	public String getPassword() {
		return  Util.toString(password);
	}
}

package core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Login_Request implements Message{

	byte[] username = new byte[16];
	byte[] password = new byte[25];
	byte type = (byte) Message.Type.LOGIN_REQUEST.ordinal();
	
	private Login_Request() {
		
	}
	
	public Login_Request(String username, String password){
		Util.strncpy(this.username,username );
		Util.strncpy(this.password,password);
	}
	
	public void write(OutputStream os) throws IOException{
		int size = 43;
		byte[] payload = { 43, type};
		os.write(payload);
		os.write(username);
		os.write(password);
	}
	
	public static Login_Request read(InputStream is) throws IOException {
		Login_Request req = new Login_Request();
		is.read(req.username);
		is.read(req.password);
		return req;
	}
	
	public String getUserName() {
		return new String(username);
	}
	
	public String getPassword() {
		return new String(password);
	}
}

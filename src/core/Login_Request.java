package core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Login_Request extends Message{

	byte[] username = new byte[USERNAME_SIZE];
	byte[] password = new byte[PASSWORD_SIZE];
		
	
	private Login_Request() {
		super(Type.LOGIN_REQUEST);
	}
	
	public Login_Request(String username, String password){
		super(Type.LOGIN_REQUEST);
		Util.strncpy(this.username,username );
		Util.strncpy(this.password,password);
	}
	
	public void write(OutputStream os) throws IOException{
		byte size = USERNAME_SIZE+ PASSWORD_SIZE;
		byte[] payload = { size, type};
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
		return  Util.toString(username);
	}
	
	public String getPassword() {
		return  Util.toString(password);
	}
}

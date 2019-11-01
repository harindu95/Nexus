package core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Message {
	enum Type {
		LOGIN_REQUEST,
		LOGIN_REPLY,
	}
	public void write(OutputStream os) throws IOException;
	
}

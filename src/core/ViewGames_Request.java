package core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import core.Message.Type;

public class ViewGames_Request extends Message {
		
	public ViewGames_Request() {
		super(Type.VIEWGAMES_REQUEST);
	}
	
	public void write(ByteArrayOutputStream os) throws IOException{
		byte size = 0;
		byte[] payload = {size, type};
		os.write(payload);
	}
}

package core;

import java.io.IOException;
import java.io.OutputStream;

public class ViewGames_Request implements Message {
	byte type = (byte)Type.VIEWGAMES_REQUEST.ordinal();
	public void write(OutputStream os) throws IOException{
		byte size = 2;
		byte[] payload = {size, type};
		os.write(payload);
	}
}

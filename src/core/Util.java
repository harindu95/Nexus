package core;

import java.nio.ByteBuffer;
import java.util.Optional;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Util {
	static void strncpy(byte[] dest, String source) {
		char[] chars = source.toCharArray();
		for(int i=0;i<dest.length; i++) {
			if(i >= chars.length) {
				dest[i] = '\0';
			}else {
				dest[i] = (byte) chars[i];
			}
		}
	}
	
	public static byte[] strByteArray(String source, int length) {
		byte[] dest = new byte[length];
		strncpy(dest,source);
		return dest;
	}
	
	public static String toString(byte[] source) {
		StringBuffer buf = new StringBuffer();
		for(int i=0;i<source.length;i++) {
			if(source[i] == 0) {
				break;
			}else {
				buf.append((char)source[i]);
			}
		}
		return buf.toString();
	}
	
	 public static int unsignedToBytes(byte b) {
		    return b & 0xFF;
	 }

	public static short toShort(byte[] header) {
		
		ByteBuffer buf = ByteBuffer.allocate(2);
		buf.put(header[0]);
		buf.put(header[1]);
		buf.flip();
		return buf.getShort();
		
	}

	public static byte[] toBytes(short size) {
		ByteBuffer buf = ByteBuffer.allocate(2);
		buf.putShort(size);
		buf.flip();
		byte[] array =  buf.array();	
		return array;
	}
	
	public static void log(byte[] data) {
		for(byte b: data) {
			System.out.print(b);
		}
		System.out.println();
	}
	
	static int error_counter = 0;
	public static  void showDialog(String error) {
		error_counter++;
		if(error_counter > 30)
			return;
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				Optional<ButtonType> b = new Alert(Alert.AlertType.ERROR, error).showAndWait();
			}
		});
		
	}
}

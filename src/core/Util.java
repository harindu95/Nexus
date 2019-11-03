package core;

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
}

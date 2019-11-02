package core;

public class Util {
	static void strncpy(byte[] dest, String source) {
		byte[] s = source.getBytes();
		for(int i=0;i<dest.length; i++) {
			if(i >= s.length) {
				dest[i] = 0;
			}else {
				dest[i] = s[i];
			}
		}
	}
	
	static byte[] strByteArray(String source, int length) {
		byte[] dest = new byte[length];
		strncpy(dest,source);
		return dest;
	}
}

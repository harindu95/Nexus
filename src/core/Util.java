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
}

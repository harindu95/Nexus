package client;

import core.Util;

public class Test {

	public static void main(String[] args) {
		String txt = "Hello";
		byte[] array = Util.strByteArray(txt, 16);
		String output = Util.toString(array);
		System.out.println(txt + "-" + output + "****");
		System.out.println(txt.equals(output));
	}
}

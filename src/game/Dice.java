package game;

public class Dice {
	int value = 0;
	
	public int roll() {
		int r = (int)(Math.random() * 5 + 1);
		value = r;
		return value;
	}
}

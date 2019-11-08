package game;

import java.util.ArrayList;
import java.util.List;

import game.Tile.Place;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class Board {
	List<Tile> tiles;
	List<Block> blocks;
	Dice dice;

	public Board() {
		tiles = new ArrayList<>();
		blocks = new ArrayList<>();
		dice = new Dice();
		init();
	}

	public void init() {

		tiles.add(new Tile("GO", 0));
		tiles.add(new Tile("Chance\n?", 0));
		Block r = new Block(Color.DARKRED);
		tiles.add(new Tile("NewYork\nAve", 300).setBlock(r));
		tiles.add(new Tile("Tennessee\nStreet", 300).setBlock(r));
		tiles.add(new Tile("St.James\nPlace", 300).setBlock(r));
		blocks.add(r);
		tiles.add(new Tile("Go to\nJail", 50));
		tiles.add(new Tile("Income\nTax", 50));
	
		Block b = new Block(Color.AQUA);
		tiles.add(new Tile("Leicester\nSquare", 800).setBlock(b));
		tiles.add(new Tile("Bow\nStreet", 1000).setBlock(b));
		tiles.add(new Tile("WhiteChapel\nRoad", 1000).setBlock(b));
		blocks.add(b);
		
		tiles.add(new Tile("JAIL", 0));
		
		Block p = new Block(Color.DEEPPINK);
		tiles.add(new Tile("The Angel\nIslignton", 250).setBlock(p));
		tiles.add(new Tile("Northhum'd\nAvenue", 350).setBlock(p));
		tiles.add(new Tile("Trafalgar\nSquare", 150).setBlock(p));
		blocks.add(p);
		tiles.add(new Tile("Community\nChest", 0));
		tiles.add(new Tile("Free\nParking", 0));

		Block y = new Block(Color.DARKGREY);
		tiles.add(new Tile("Fleet\nStreet", 300).setBlock(y));
		tiles.add(new Tile("Old Kent\nStreet", 300).setBlock(y));
		tiles.add(new Tile("M'Borough\nStreet", 300).setBlock(y));
		blocks.add(y);
		tiles.add(new Tile("Community\nChest", 0));
//		tiles.add(new Tile("Train\nStation", 400));

		setLocations();
	}
	
	public void setLocations() {
		int i=0;
		
		for(Tile t:tiles) {
			if( i < 6) {
				int x = Tile.width *(5 - i);
				int y = 500;
				Place p = Place.BOTTOM;
				t.setLocation(x, y, p);
			}else if(i < 10) {
				int x = 0;
				int y = 500 - Tile.height*(i- 5);
				Place p = Place.LEFT;
				t.setLocation(x, y, p);
			}else if(i <16) {
				int x = Tile.width*(i - 10 );
				int y = 0;
				Place p = Place.TOP;
				t.setLocation(x, y, p);
			}else if(i < 20) {
				int x = Tile.width	* 5;
				int y = 100 * ( i - 15);
				Place p = Place.RIGHT;
				t.setLocation(x, y, p);
			}
			i++;
		}
	}
	
	public void draw(GraphicsContext gc) {
		gc.setFill(Color.AQUAMARINE);
		gc.fillRect(0, 0, 600, 600);
		gc.setFill(Color.RED);
		Font f = gc.getFont();
		gc.setFont(new Font(30));
		gc.fillText("MONOPOLY", 200, 300);
		gc.setFont(f);
		for(Tile t: tiles) {
			t.draw(gc);
		}
		
	}
	
	public int getCenterX(int index) {
		Tile t = tiles.get(index);
		int x = t.x + (t.width /2 );
		return x;
	}
	
	public int getCenterY(int index) {
		Tile t = tiles.get(index);
		int y = t.y + (t.height /2 );
		return y;
	}

	public Tile getTile(int index) {
		
		return tiles.get(index);
	}
	
	public int rollDice() {
		return dice.roll();
	}

	public int getSize() {
		return tiles.size();
	}
	
	public int getIndex(Tile t) {
		return tiles.indexOf(t);
	}
	
	
}

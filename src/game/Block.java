package game;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Block {
	
	List<Tile> tiles;
	Color color;
	
	Block(Color c){
		tiles = new ArrayList<>();
		color = c;
	}
	
	
	
	public void add(Tile t) {
		t.setColor(color);
		tiles.add(t);
	}
	
	public void draw(GraphicsContext gc) {
		
		for(Tile t: tiles) {
			t.draw(gc);
		}
		
	}
	
}

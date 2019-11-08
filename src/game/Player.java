package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Player {

	String username;
	int position = 0;
	double money = 5000;
	private int id = 0;
	static Color[] colors = {
		Color.BLUE,
		Color.VIOLET,
		Color.ORANGE,
		Color.DARKGREEN,
		Color.LAVENDER,
		Color.SEAGREEN,
		Color.LIGHTCYAN,
		Color.YELLOWGREEN
	};
	
	List<Tile> owned;
	
	Board board;
	
	public Player(String username) {
		owned = new ArrayList<Tile>();
		this.username = username;
	}
	
	public Player(String username, Board map) {
		board = map;
		owned = new ArrayList<Tile>();
		this.username = username;
	}
	
	public void draw(GraphicsContext gc){
		gc.setFill(getColor());
		int x = board.getCenterX(position);
		int y = board.getCenterY(position);
		gc.fillOval(x, y, 20, 20);
	}
	
	public void move(int dice) {
		position = (position + dice) % board.getSize();
	}

	private void land() {
		Tile t = board.getTile(position);
		if(t.owner == null) {
			
		}else {
			if(t.owner != this) {
				t.payRent(this);
			}
		}
		
	}
	
	public void withdraw(double price) {
		
		if(price < money) {
			money -= price;
			System.out.println("Money: " + money);
		}
	}

	public void addMoney(double rent) {
		money += rent;
		
	}
	
	public Color getColor() {
		return colors[id];
	}
	
	public void buy() {
		Tile tile = board.getTile(position);
		if(tile.getPrice() < money) {
			money -= tile.getPrice();
			addTile(tile);
			
		}else {
//			TODO: Not enough money
		}
		
	}
	
	public void addTile(Tile tile) {
		tile.owner = this;
		owned.add(tile);
	}
	
	
	public String getProperties() {
		String format = "Title Deeds: \n";
		for(Tile t: owned) {
			format += "  # " + t.getTitle() + "\n";
		}
		
		
		return format;
				
	}

	public String getName() {
		return username;
	}

	public double getMoney() {
		
		return money;
	}

	public Tile getLocation() {
		return board.getTile(position);
	}

	public int getPosition() {
		return position;
		
	}

	public Board getBoard() {
		
		return board;
	}

	public int[] getOwnedProperties() {
		int[] tiles = new int[owned.size()];
		int i =0;
		for(Tile t:owned) {
			tiles[i] = board.getIndex(t);
			i++;
		}
		return tiles;
	}

	public void setPosition(byte pos) {
		position = pos;
	}

	public void setMoney(double m) {
		money = m;
		
	}

	int[] properties;
	public void setProperties(int[] o) {
		properties = o;
		
	}

	public void setBoard(Board board) {
		this.board = board;
		for(int i: properties) {
			addTile(board.getTile(i));
		}
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
package game;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Tile {
	int x, y;
	private String title;
	private double price;
	static int width = 100;
	static int height = 100;
	Color color = null;
	private Place place;
	Block block = null;
	Player owner = null;
	List<Player> players = new ArrayList<>();

	enum Place {
		TOP, BOTTOM, LEFT, RIGHT
	};

	Tile(String title, double price) {
		this.title = title;
		this.setPrice(price);

	}

	public void setLocation(int x, int y, Place p) {
		this.x = x;
		this.y = y;
		this.place = p;
	}

	public Tile setColor(Color c) {
		this.color = c;
		return this;
	}

	public void draw(GraphicsContext gc) {
		gc.setStroke(Color.BLACK);
		gc.setFill(Color.WHITE);
		gc.fillRect(x, y, width, height);
		gc.strokeRect(x, y, width, height);

		double center = height / 3;
		double margin = width / 4;
		drawMarker(gc);

		gc.setFill(Color.BLACK);
		gc.fillText(String.format("%s", title), x + margin, y + center);

		if (owner != null) {
			double[] xPoints = { x, x + 20, x };
			double[] yPoints = { y, y, y + 20 };
			gc.setFill(owner.getColor());
			gc.fillPolygon(xPoints, yPoints, 3);
		}

	}

	public void drawMarker(GraphicsContext gc) {
		int x = 0;
		int y = 0;
		int height = 0;
		int width = 0;
		switch (place) {
		case TOP:
			x = this.x;
			y = this.y + this.height - 20;
			height = 20;
			width = this.width;
			break;
		case BOTTOM:
			x = this.x;
			y = this.y;
			height = 20;
			width = this.width;
			break;
		case LEFT:
			x = this.x + this.width - 20;
			y = this.y;
			height = this.height;
			width = 20;
			break;
		case RIGHT:
			x = this.x;
			y = this.y;
			width = 20;
			height = this.height;
			break;
		}
		if (color != null) {
			gc.setFill(color);
			gc.fillRect(x, y, width, height);
			gc.setFill(Color.BLACK);
		}
	}

	public Tile setBlock(Block b) {
		block = b;
		block.add(this);
		return this;
	}


	public void payRent(Player p) {
		double rent = getPrice() * 0.1;
		if (getPrice() > 0) {
			p.withdraw(rent);
			owner.addMoney(rent);
		}
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getTitle() {
		return title;
	}
	
	public String toString() {
		if(price >0) {
			return String.format("%s $ %.2f", title, price);
		}else {
			return title;
		}
	}
	
	public boolean canBuy() {
		return (price > 0 ) && owner == null;
	}
	
	public boolean canRent() {
		return owner != null;
	}
}

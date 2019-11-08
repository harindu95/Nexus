package core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import game.Player;
import game.Tile;

public class GameState extends Message{

	List<Player> players;
	
	GameState() {
		super(Type.GAMESTATE);
	}

	@Override
	public void write(ByteArrayOutputStream os) throws IOException {
		for(Player p: players){
			p.getName();
			p.getPosition();
			p.getMoney();
			int[] owned = p.getOwnedProperties();
		}
		
	}

}

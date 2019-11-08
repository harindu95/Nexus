package core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import game.Player;
import game.Tile;

public class GameState extends Message{

	List<Player> players;
	
	private GameState() {
		super(Type.GAMESTATE);
		players = new ArrayList<Player>();
	}
	
	public GameState(GameRoom room) {
		super(Type.GAMESTATE);
		players = room.serverGame.getPlayers();
	}

	@Override
	public void write(ByteArrayOutputStream os) throws IOException {
		byte numPlayers = (byte)players.size();
		os.write(numPlayers);
		for(Player p: players){
			byte[] username = Util.strByteArray(p.getName(), USERNAME_SIZE);
			byte pos = (byte)p.getPosition();
			double d = p.getMoney();
			int[] owned = p.getOwnedProperties();
			os.write(username);
			os.write(pos);
			os.write(ByteBuffer.allocate(8).putDouble(d).array());
			byte numProperties = (byte)owned.length;
			os.write(numProperties);
			for(int o: owned) {
				os.write(o);
			}
		}
		
	}
	
	public static GameState read(ByteArrayInputStream is) throws IOException{
		byte numPlayers = (byte)is.read();
		GameState msg = new GameState();
		for(int i=0; i<numPlayers;i++) {
			byte[] username = new byte[USERNAME_SIZE];
			is.read(username);
			byte pos = (byte)is.read();
			byte[] money = new byte[8];
			is.read(money);
			double m = ByteBuffer.wrap(money).getDouble();
			byte numProperties = (byte) is.read();
			int[] owned = new int[numProperties];
			for(int j=0;j<numProperties;j++) {
				owned[i] = is.read();
			}
			Player p = new Player(Util.toString(username));
			p.setPosition(pos);
			p.setMoney(m);
			p.setProperties(owned);
			msg.players.add(p);
		}
		
		return msg;
		
	}

	public List<Player> getPlayers() {
		
		return players;
	}

}


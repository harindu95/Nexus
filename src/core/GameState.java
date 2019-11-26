package core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import game.Player;
import game.Tile;
import ui.Game;

public class GameState extends Message{

	List<Player> players;
	byte gameId;
	byte turn = 0;
	
	GameState() {
		super(Type.GAMESTATE);
		players = new ArrayList<Player>();
	}
	
	public GameState(GameRoom room) {
		super(Type.GAMESTATE);
		players = room.serverGame.getPlayers();
		turn = (byte) room.serverGame.getTurn();
		gameId = (byte)room.id;
	}

	@Override
	public void write(ByteArrayOutputStream os) throws IOException {
		byte numPlayers = (byte)players.size();
		os.write(gameId);
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
		
		os.write(turn);
		
	}
	
	public static GameState read(ByteArrayInputStream is) throws IOException{
		byte gameId = (byte) is.read();
		byte numPlayers = (byte)is.read();
		GameState msg = new GameState();
		msg.gameId = gameId;
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
				owned[j] = is.read();
			}
			Player p = new Player(Util.toString(username));
			p.setPosition(pos);
			p.setMoney(m);
			p.setProperties(owned);
			msg.players.add(p);
		}
		
		msg.turn = (byte)is.read();
		
		return msg;
		
	}

	public List<Player> getPlayers() {
		
		return players;
	}

	public int getGameId() {
		return gameId;
	}
	
//	public String toString() {
//		String output  = "Turn " + turn;
//		for(Player p: players) {
//			output += p.getId() +"-" + p.getName() + " : " + p.getMoney() + "| " + p.properties.size() + "\n";
//		}
//		return output;
//		
//	}

	public static GameState fromGame(Game game) {
		GameState state = new GameState();
		state.players = game.getPlayers();
		state.gameId = (byte) game.getId();
		state.turn = (byte)game.getTurn();
		return state;
	}

	public int getTurn() {
		return turn;
	}
}


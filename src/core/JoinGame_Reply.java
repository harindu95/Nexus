package core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class JoinGame_Reply implements Message {
	
	byte type = (byte) Type.JOINGAME_REPLY.ordinal();
	byte status = 0;
	private GameRoom game;

	private JoinGame_Reply() {
		
	}
	
	public JoinGame_Reply(GameRoom g) {
		this.game = g;
	}

	@Override
	public void write(OutputStream os) throws IOException {
		byte gameId = (byte)game.id;
		byte max = (byte)game.maxPlayers;
		byte numPlayers = (byte)game.users.size();
		byte size = (byte) (30 + 16 * numPlayers + 2 + 2);
		byte[] header = { size, type, status};
		os.write(header);
		byte[] gameName = Util.strByteArray(game.getName(), 30);
		os.write(gameName);
		byte[] payload = {gameId, max, numPlayers};
		os.write(payload);
		List<User> players = game.getUsers();
		for(User p: players) {
			byte[] username = new byte[16];
			Util.strncpy(username, p.getUsername());
			os.write(username);
		}
		
	}
	
	public static JoinGame_Reply read(InputStream is) throws IOException{
		JoinGame_Reply reply = new JoinGame_Reply();
		byte[] status = new byte[1];
		is.read(status);
		byte[] gameName = new byte[30];
		is.read(gameName);
		byte[] header = new byte[3];
		is.read(header);
		byte gameId = header[0];
		int max = header[1];
		int numPlayers = header[2];
		
		reply.game = new GameRoom( Util.toString(gameName),max);
		reply.game.setId(gameId);
		for(int i=0;i <numPlayers;i++ ) {
			byte[] username = new byte[16];
			is.read(username);
			User u = new User( Util.toString(username));
			reply.game.join(u);
		}
		
		return reply;
	}
	
	public GameRoom getGame() {
		return game;
	}

}

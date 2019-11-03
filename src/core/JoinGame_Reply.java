package core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import core.Message.Type;

public class JoinGame_Reply extends Message {
	
	byte status = 0;
	private GameRoom game;

	private JoinGame_Reply() {
		super(Type.JOINGAME_REPLY);
	}
	
	public JoinGame_Reply(GameRoom g) {
		super(Type.JOINGAME_REPLY);
		this.game = g;
	}

	@Override
	public void write(ByteArrayOutputStream os) throws IOException {
		byte gameId = (byte)game.id;
		byte max = (byte)game.maxPlayers;
		byte numPlayers = (byte)game.users.size();
		byte size = (byte) (GAMENAME_SIZE + USERNAME_SIZE * numPlayers + GAMEID_SIZE + MAXPLAYERS_SIZE + STATUS);
		byte[] header = { size, type, status};
		os.write(header);
		byte[] gameName = Util.strByteArray(game.getName(), GAMENAME_SIZE);
		os.write(gameName);
		byte[] payload = {gameId, max, numPlayers};
		os.write(payload);
		List<User> players = game.getUsers();
		for(User p: players) {
			byte[] username = new byte[USERNAME_SIZE];
			Util.strncpy(username, p.getUsername());
			os.write(username);
		}
		
	}
	
	public static JoinGame_Reply read(ByteArrayInputStream is) throws IOException{
		JoinGame_Reply reply = new JoinGame_Reply();
		byte[] status = new byte[STATUS];
		is.read(status);
		byte[] gameName = new byte[GAMENAME_SIZE];
		is.read(gameName);
		byte[] header = new byte[3];
		is.read(header);
		byte gameId = header[0];
		int max = header[1];
		int numPlayers = header[2];
		
		reply.game = new GameRoom( Util.toString(gameName),max);
		reply.game.setId(gameId);
		for(int i=0;i <numPlayers;i++ ) {
			byte[] username = new byte[USERNAME_SIZE];
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

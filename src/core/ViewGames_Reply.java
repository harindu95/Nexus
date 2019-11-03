package core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import core.Message.Type;

public class ViewGames_Reply extends Message{

	List<GameRoom> games;
	
	public List<GameRoom> getGames() {
		return games;
	}

	public void setGames(List<GameRoom> games) {
		this.games = games;
	}


	byte status = 0;
	
	public ViewGames_Reply(List<GameRoom> games) {
		super(Type.VIEWGAMES_REPLY);
		this.games = games;
	}

	private ViewGames_Reply() {
		super(Type.VIEWGAMES_REPLY);
		games = new ArrayList<GameRoom>();
	}
	
	@Override
	public void write(ByteArrayOutputStream os) throws IOException {
		byte numGames = (byte) games.size();
		byte[] header = {status, numGames};
		os.write(header);
		for(GameRoom g: games) {
			byte id = (byte) g.id;
			byte max = (byte) g.maxPlayers;
			byte[] info = {id, max};
			os.write(info);
			byte[] name = new byte[GAMENAME_SIZE];
			Util.strncpy(name, g.name);
			os.write(name);			
		}
			
	}
	
	
	public static ViewGames_Reply read(ByteArrayInputStream is) throws IOException{
		ViewGames_Reply reply = new ViewGames_Reply();
		byte[] header = new byte[2];
		is.read(header);
		reply.status = header[0];
		int numGames = header[1];
		for(int i=0; i< numGames;i++) {
			byte[] info = new byte[2];
			is.read(info);
			byte[] name = new byte[GAMENAME_SIZE];
			is.read(name);
			byte id = info[0];
			byte max = info[1];
			GameRoom g = new GameRoom( Util.toString(name), max);
			g.setId(id);
			reply.games.add(g);
		}
				
		return reply;
	}
	
	
}

package core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import core.Message.Type;

public class ViewGames_Reply implements Message{

	List<Game> games;
	
	public List<Game> getGames() {
		return games;
	}

	public void setGames(List<Game> games) {
		this.games = games;
	}


	byte type = (byte)Type.VIEWGAMES_REPLY.ordinal();
	byte status = 0;
	
	public ViewGames_Reply(List<Game> games) {
		this.games = games;
	}

	private ViewGames_Reply() {
		games = new ArrayList<Game>();
	}
	
	@Override
	public void write(OutputStream os) throws IOException {
		byte numGames = (byte) games.size();
		byte size = (byte) (numGames * (30 + 1 + 1) + 3);
		byte[] header = {size, type, status, numGames};
		os.write(header);
		for(Game g: games) {
			byte id = (byte) g.id;
			byte max = (byte) g.maxPlayers;
			byte[] info = {id, max};
			os.write(info);
			byte[] name = new byte[30];
			Util.strncpy(name, g.name);
			os.write(name);			
		}
			
	}
	
	
	public static ViewGames_Reply read(InputStream is) throws IOException{
		ViewGames_Reply reply = new ViewGames_Reply();
		byte[] header = new byte[2];
		is.read(header);
		reply.status = header[0];
		int numGames = header[1];
		for(int i=0; i< numGames;i++) {
			byte[] info = new byte[2];
			is.read(info);
			byte[] name = new byte[30];
			is.read(name);
			byte id = info[0];
			byte max = info[1];
			Game g = new Game(new String(name), max);
			g.setId(id);
			reply.games.add(g);
		}
				
		return reply;
	}
	
	
}

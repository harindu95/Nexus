package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

import core.ChatMessage;
import core.CreateGame_Request;
import core.JoinGame_Request;
import core.Login_Request;
import core.Message;
import core.OnlineUsers_Request;
import core.ViewGames_Request;

public class Connection extends Thread {

	InputStream is;
	OutputStream os;
	Message output = null;
	Application app;
	Socket socket;

	public Connection(Socket socket) throws IOException {
		this.socket = socket;
		socket.setSoTimeout(100);
		is = socket.getInputStream();
		os = socket.getOutputStream();
		System.out.println("Connection established");
		app = new Application(this);
	}

	public void run() {

		while (true) {
			if (output == null) {
				byte[] header = new byte[2];
				try {
					int length = is.read(header);
					if (length < 0) {
						// Connection offline
						break;
					}
					int size = header[0];
					byte t = header[1];
					if(t >= Message.Type.values().length)
						continue;
					Message.Type type = Message.Type.values()[t];
					System.out.println("Reading message");
					if (type == Message.Type.LOGIN_REQUEST) {
						Login_Request req = Login_Request.read(is);
						System.out.println("Username : " + req.getUserName());
						app.handle(req);
					}else if(type == Message.Type.ONLINEUSERS_REQUEST) {
						OnlineUsers_Request req = new OnlineUsers_Request();
						System.out.println("Reading ONLINEUSERS_REQUEST");
						app.handle(req);
					}else if(type == Message.Type.CREATEGAME_REQUEST) {
						CreateGame_Request req = CreateGame_Request.read(is);
						System.out.println("Reading CREATEGAME_REQUEST");
						app.handle(req);
					}else if(type == Message.Type.VIEWGAMES_REQUEST) {
						ViewGames_Request req = new ViewGames_Request();
						System.out.println("Reading VIEWGAMES_REQUEST");
						app.handle(req);
					}else if(type == Message.Type.JOINGAME_REQUEST) {
						JoinGame_Request req = JoinGame_Request.read(is);
						System.out.println("Reading JOINGAME_REQUEST");
						app.handle(req);
					}else if(type == Message.Type.CHATMESSAGE) {
						ChatMessage msg = ChatMessage.read(is);
						System.out.println("Reading CHATMESSAGE");
						app.handle(msg);
					}
				} catch (SocketTimeoutException e) {

//					System.out.println("Socket timeout");
				} catch (IOException e) {

					
					e.printStackTrace();
					break;
				}

			} else {
				synchronized (output) {
					writeMessage(output);
				}
			}
		}

	}

	void writeMessage(Message msg) {

		try {
			msg.write(os);
			System.out.println("Message sent");
		} catch (IOException e) {
		
			e.printStackTrace();

		}
	}
}

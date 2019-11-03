package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ConcurrentLinkedQueue;

import core.ChatMessage;
import core.CreateGame_Request;
import core.JoinGame_Request;
import core.Login_Request;
import core.Logout_Request;
import core.Message;
import core.OnlineUsers_Request;
import core.ViewGames_Request;

public class Connection extends Thread {

	InputStream is;
	OutputStream os;
	Application app;
	Socket socket;
	private ConcurrentLinkedQueue<Message> output;

	public Connection(Socket socket) throws IOException {
		this.socket = socket;
		socket.setSoTimeout(100);
		is = socket.getInputStream();
		os = socket.getOutputStream();
		System.out.println("Connection established");
		app = new Application(this);
		output = new ConcurrentLinkedQueue<Message>();
	}

	public void run() {

		while (true) {
			if (output.size() > 0) {
				synchronized (output) {
					writeMessage(output.remove());
				}
			} else {
				byte[] header = new byte[2];
				try {
					int length = is.read(header);
					if (length < 0) {
						// Connection offline
						//TODO: Disconnected 
						break;
					}
					int size = header[0];
					byte t = header[1];
					if (t >= Message.Type.values().length)
						continue;
					Message.Type type = Message.Type.values()[t];
					System.out.println("Reading message :" + type.toString());
					if (type == Message.Type.LOGIN_REQUEST) {
						Login_Request req = Login_Request.read(is);
						app.handle(req);
					} else if (type == Message.Type.ONLINEUSERS_REQUEST) {
						OnlineUsers_Request req = new OnlineUsers_Request();
						app.handle(req);
					} else if (type == Message.Type.CREATEGAME_REQUEST) {
						CreateGame_Request req = CreateGame_Request.read(is);
						app.handle(req);
					} else if (type == Message.Type.VIEWGAMES_REQUEST) {
						ViewGames_Request req = new ViewGames_Request();
						app.handle(req);
					} else if (type == Message.Type.JOINGAME_REQUEST) {
						JoinGame_Request req = JoinGame_Request.read(is);
						app.handle(req);
					} else if (type == Message.Type.CHATMESSAGE) {
						ChatMessage msg = ChatMessage.read(is);
						app.handle(msg);
					} else if (type == Message.Type.LOGOUT_REQUEST) {
						Logout_Request req = Logout_Request.read(is);
						app.handle(req);
					}
				} catch (SocketTimeoutException e) {

//					System.out.println("Socket timeout");
				} catch (IOException e) {

					e.printStackTrace();
					break;
				}

			}
		}

	}

	void writeMessage(Message msg) {

		try {
			msg.write(os);
			System.out.println("Sent :" + msg.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

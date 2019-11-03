package server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
import core.Reconnect_Request;
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
						// TODO: Disconnected
						break;
					}
					int size = header[0];
					byte t = header[1];
					if (t >= Message.Type.values().length)
						continue;
					Message.Type type = Message.Type.values()[t];
					System.out.println("Reading message :" + type.toString());
					byte[] payload = new byte[size];
					int read = is.read(payload);
					if (read != size) {
						// BUG
						System.err.println("BUG: Size mismatch");
					} else {
						Message msg = null;
						ByteArrayInputStream buf = new ByteArrayInputStream(payload);
						if (type == Message.Type.LOGIN_REQUEST) {
							msg = Login_Request.read(buf);
						} else if (type == Message.Type.ONLINEUSERS_REQUEST) {
							msg = new OnlineUsers_Request();
						} else if (type == Message.Type.CREATEGAME_REQUEST) {
							msg = CreateGame_Request.read(buf);
						} else if (type == Message.Type.VIEWGAMES_REQUEST) {
							 msg = new ViewGames_Request();
						} else if (type == Message.Type.JOINGAME_REQUEST) {
							 msg = JoinGame_Request.read(buf);
						} else if (type == Message.Type.CHATMESSAGE) {
							 msg = ChatMessage.read(buf);
						} else if (type == Message.Type.LOGOUT_REQUEST) {
							 msg = Logout_Request.read(buf);
						} else if (type == Message.Type.RECONNECT_REQUEST) {
							 msg = Reconnect_Request.read(buf);
						}
						app.handle(msg);
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

	public void send(Message msg) {
		output.add(msg);
	}

	void writeMessage(Message msg) {

		try {
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
			msg.write(buf);
			os.write(buf.toByteArray());
			System.out.println("Sent :" + msg.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

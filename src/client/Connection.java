package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import core.ChatMessage;
import core.JoinGame_Reply;
import core.Login_Reply;
import core.Logout_Reply;
import core.Message;
import core.OnlineUsers_Reply;
import core.Reconnect_Reply;
import core.ViewGames_Reply;

public class Connection implements Runnable {

	InputStream is;
	OutputStream os;
	public Queue<Message> output = null;
	Application app;

	public Connection(Application app) throws UnknownHostException, IOException {
		this.app = app;
		init();
	}

	public void run() {

		while (true) {
			if (output.size() > 0) {
				System.out.println("Message available");
				writeMessage(output.remove());
			} else {
				byte[] header = new byte[2];
				try {
					int length = is.read(header);
					if (length < 2) {
						if (length == -1) {
							// Disconnected !!
							app.reconnect();
						}
					}
					int size = header[0];
					byte t = header[1];
					if (t >= Message.Type.values().length)
						continue;
					Message.Type type = Message.Type.values()[t];
					System.out.println("Reading message :" + type.toString());
					if (type == Message.Type.LOGIN_REPLY) {
						Login_Reply reply = Login_Reply.read(is);
						app.handle(reply);
					} else if (type == Message.Type.ONLINEUSERS_REPLY) {
						OnlineUsers_Reply reply = OnlineUsers_Reply.read(is);
						app.handle(reply);
					} else if (type == Message.Type.VIEWGAMES_REPLY) {
						ViewGames_Reply reply = ViewGames_Reply.read(is);
						app.handle(reply);
					} else if (type == Message.Type.JOINGAME_REPLY) {
						System.out.println("JOINGAME_REPLY");
						JoinGame_Reply reply = JoinGame_Reply.read(is);
						app.handle(reply);
					} else if (type == Message.Type.CHATMESSAGE) {
						ChatMessage msg = ChatMessage.read(is);
						app.handle(msg);
					} else if (type == Message.Type.LOGOUT_REPLY) {
						Logout_Reply reply = Logout_Reply.read(is);
						app.handle(reply);
					}else if(type == Message.Type.RECONNECT_REPLY) {
						Reconnect_Reply reply = Reconnect_Reply.read(is);
						app.handle(reply);
					}

				} catch (SocketTimeoutException e) {

				} catch (IOException e) {

					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	public void send(Message msg) {
		output.add(msg);
	}

	void writeMessage(Message msg) {

		try {
			msg.write(os);
			System.out.println("Sent :" + msg.toString());

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	private void init() throws IOException {
		// Initialize a client socket connection to the server
		Socket clientSocket = new Socket("0.0.0.0", 3000);

		// Initialize input and an output stream for the connection(s)
		os = clientSocket.getOutputStream();

		is = clientSocket.getInputStream();
		clientSocket.setSoTimeout(50);
		output = new ConcurrentLinkedQueue<Message>();

	}
	
	public void reset() throws IOException {
		init();
	}
}

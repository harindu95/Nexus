package client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import core.ChatMessage;
import core.GameState;
import core.JoinGame_Reply;
import core.Login_Reply;
import core.Logout_Reply;
import core.Message;
import core.OnlineUsers_Reply;
import core.Reconnect_Reply;
import core.RollDice;
import core.Util;
import core.ViewGames_Reply;

public class Connection implements Runnable {

	InputStream is;
	OutputStream os;
	public Queue<Message> output = null;
	Application app;
	private int sentBytes;
	private int recvBytes;

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
					int size = Util.unsignedToBytes(header[0]);
					byte t = header[1];
					if (t >= Message.Type.values().length || t <= 0 || size < 0) {
						System.err.println("Invalid header !!" + "  " + size + "Bytes | " + t + " Type");
						continue;
					}
					Message.Type type = Message.Type.values()[t];
					System.out.println("Reading message :" + type.toString() + " | " + size + " Bytes");
					byte[] payload = new byte[size];
					int read = is.read(payload);
					recvBytes += read;
					if (read != size) {
						// BUG
						System.err.println("BUG: Size mismatch");
					} else {
						Message msg = null;
						ByteArrayInputStream buf = new ByteArrayInputStream(payload);
						if (type == Message.Type.LOGIN_REPLY) {
							msg = Login_Reply.read(buf);
						} else if (type == Message.Type.ONLINEUSERS_REPLY) {
							msg = OnlineUsers_Reply.read(buf);
						} else if (type == Message.Type.VIEWGAMES_REPLY) {
							msg = ViewGames_Reply.read(buf);
						} else if (type == Message.Type.JOINGAME_REPLY) {
							msg = JoinGame_Reply.read(buf);
						} else if (type == Message.Type.CHATMESSAGE) {
							msg = ChatMessage.read(buf);
						} else if (type == Message.Type.LOGOUT_REPLY) {
							msg = Logout_Reply.read(buf);
						} else if (type == Message.Type.RECONNECT_REPLY) {
							msg = Reconnect_Reply.read(buf);
						}else if(type == Message.Type.ROLLDICE) {
							msg = RollDice.read(buf);
						}else if(type == Message.Type.GAMESTATE) {
							msg = GameState.read(buf);
						}

						app.handle(msg);
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
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
			msg.write(buf);
			byte size = (byte) buf.size();
			byte type = msg.getType();
			byte[] header = { size, type };
			os.write(header);
			os.write(buf.toByteArray());
			sentBytes += buf.size();
			System.out.println("Sent :" + msg.toString() + " | " + buf.size() + " Bytes");

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

	public int getSentBytes() {
		int t = sentBytes;
		sentBytes = 0;
		return t;
	}

	public int getRecvBytes() {
		int t = recvBytes;
		recvBytes = 0;
		return t;
	}
}

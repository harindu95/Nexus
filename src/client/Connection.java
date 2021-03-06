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
import core.ObserveGame_Reply;
import core.OnlineUsers_Reply;
import core.Reconnect_Reply;
import core.Util;
import core.ViewGames_Reply;

public class Connection implements Runnable {

	InputStream is;
	OutputStream os;
	public Queue<Message> output = null;
	Application app;
	private int sentBytes;
	private int recvBytes;
	String hostIP = "0.0.0.0";
	int hostPort = 3001;

	public Connection(Application app,String ip, int port ) throws UnknownHostException, IOException {
		this.app = app;
		hostIP = ip;
		hostPort = port;
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
					int size = Util.toShort(header);
					byte t = (byte)is.read();
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
						Util.showDialog("Message size:Size mismatch");		
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
						}else if(type == Message.Type.GAMESTATE) {
							msg = GameState.read(buf);
							
						}else if(type == Message.Type.OBSERVEGAME_REPLY) {
							msg = ObserveGame_Reply.read(buf);
						}

						app.handle(msg);
					}

				} catch (SocketTimeoutException e) {
						
				} catch (IOException e) {
					Util.showDialog(e.getMessage());		
					e.printStackTrace();
				} catch (InterruptedException e) {
										
					Util.showDialog(e.getMessage());
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
			byte[] size = Util.toBytes((short)buf.size());
			byte[] type = { msg.getType() };
			os.write(size);
			os.write(type);
			os.write(buf.toByteArray());
			sentBytes += buf.size();
//			Util.log(size);
//			Util.log(type);
//			Util.log(buf.toByteArray());
			System.out.println("Sent :" + msg.toString() + " | " + buf.size() + " Bytes");

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	private void init() throws IOException {
		// Initialize a client socket connection to the server
		Socket clientSocket = new Socket(hostIP, hostPort);

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

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
import core.GameState;
import core.JoinGame_Request;
import core.LeaveGame;
import core.Login_Request;
import core.Logout_Request;
import core.Message;
import core.ObserveGame_Request;
import core.OnlineUsers_Request;
import core.Reconnect_Request;
import core.Util;
import core.ViewGames_Request;

public class Connection extends Thread {

	InputStream is;
	OutputStream os;
	Application app;
	Socket socket;
	private ConcurrentLinkedQueue<Message> output;
	boolean socketLock = false;

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
					int length = 0;
					synchronized (socket) {
						length = is.read(header);
					}
					if (length < 0) {
						// Connection offline
						// TODO: Disconnected
						break;
					}
					int size = Util.toShort(header);
					byte t = (byte)is.read();
					if (t >= Message.Type.values().length || t <= 0 || size < 0) {
						System.err.println("Invalid header !!" +"  " + size + "Bytes | " +  t + " Type");
						continue;
					}
					Message.Type type = Message.Type.values()[t];
					System.out.println("Reading message :" + type.toString() + " | " + size + " Bytes");
					byte[] payload = new byte[size];
					synchronized (socket) {
						length = is.read(payload);
					}
					if (length != size) {
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
						}else if(type == Message.Type.GAMESTATE) {
							msg = GameState.read(buf);
						}else if(type == Message.Type.LEAVEGAME) {
							msg = LeaveGame.read(buf);
						}else if(type == Message.Type.OBSERVEGAME_REQUEST) {
							msg = ObserveGame_Request.read(buf);
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
			byte[] size = Util.toBytes((short)buf.size());
						
			byte[] type = { msg.getType() };
			
			synchronized (socket) {
				os.write(size);
				os.write(type);				
				os.write(buf.toByteArray());
			}
		
			System.out.println("Sent :" + msg.toString() + " | " + buf.size() + " Bytes");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

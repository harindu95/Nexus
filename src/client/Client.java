package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import core.CreateGame_Reply;
import core.Login_Reply;
import core.Login_Request;
import core.Message;
import core.OnlineUsers_Reply;

public class Client implements Runnable {

	InputStream is;
	OutputStream os;
	public Queue<Message> output = null;
	Application app;

	public Client(Application app) throws UnknownHostException, IOException {
		this.app = app;
		// Initialize a client socket connection to the server
		Socket clientSocket = new Socket("0.0.0.0", 3000);

		// Initialize input and an output stream for the connection(s)
		os = clientSocket.getOutputStream();

		is = clientSocket.getInputStream();
		clientSocket.setSoTimeout(50);
		output = new ConcurrentLinkedQueue<Message>();
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
					if(length < 2) {
						continue;
					}
					int size = header[0];
					Message.Type type = Message.Type.values()[header[1]];
					if (type == Message.Type.LOGIN_REQUEST) {
						Login_Request req = Login_Request.read(is);
					} else if (type == Message.Type.LOGIN_REPLY) {
						Login_Reply reply = Login_Reply.read(is);
						app.handle(reply);
					} else if (type == Message.Type.ONLINEUSERS_REPLY) {
						OnlineUsers_Reply reply = OnlineUsers_Reply.read(is);
						app.handle(reply);
					} else if(type == Message.Type.CREATEGAME_REPLY) {
						CreateGame_Reply reply = CreateGame_Reply.read(is);
						app.handle(reply);
					}
				} catch (SocketTimeoutException e) {

				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
		}

	}

	public void setOutput(Message msg) {
//		synchronized (output) {
		output.add(msg);
//		}
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

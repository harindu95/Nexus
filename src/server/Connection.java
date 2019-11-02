package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

import core.Login_Request;
import core.Message;

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
					Message.Type type = Message.Type.values()[header[1]];
					System.out.println("Reading message");
					if (type == Message.Type.LOGIN_REQUEST) {
						Login_Request req = Login_Request.read(is);
						System.out.println("Username : " + req.getUserName());
						app.handle(req);
					}
				} catch (SocketTimeoutException e) {

//					System.out.println("Socket timeout");
				} catch (IOException e) {

					// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}
}

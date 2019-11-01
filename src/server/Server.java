package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import core.Login_Request;
import core.Message;

public class Server extends Thread {

	InputStream is;
	OutputStream os;
	Message output = null;
	Application app;

	public static void main(String[] args) {
		try {
			Server s = new Server();
			s.run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Server() throws UnknownHostException, IOException {
		// Initialize a server socket
		ServerSocket serverSocket = new ServerSocket(3000);
		System.out.println("Waiting for a client ");
		Socket socket = serverSocket.accept();
		socket.setSoTimeout(50);
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
					is.read(header);
					int size = header[0];
					Message.Type type = Message.Type.values()[header[1]];
					System.out.println("Reading message");
					if (type == Message.Type.LOGIN_REQUEST) {
						Login_Request req = Login_Request.read(is);
						System.out.println("Username : " + req.getUserName());
						app.handle(req);
					}
				} catch (SocketTimeoutException e) {

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
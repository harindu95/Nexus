package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import core.Login_Request;
import core.Message;

public class Client implements Runnable {

	InputStream is;
	OutputStream os;
	public Queue<Message> output = null;


	public Client() throws UnknownHostException, IOException {
		// Initialize a client socket connection to the server
		Socket clientSocket = new Socket("0.0.0.0", 3000);

		// Initialize input and an output stream for the connection(s)
		os = clientSocket.getOutputStream();

		is = clientSocket.getInputStream();
		output = new ConcurrentLinkedQueue<Message>();
	}

	public void run() {

		while (true) {
			if (output.size() > 0) {

				System.out.println("Message available");
				writeMessage(output.remove());	

			} else {
//				byte[] header = new byte[2];
//				try {
//					is.read(header);
//					int size = header[0];
//					Message.Type type = Message.Type.values()[header[1]];
//					if(type == Message.Type.LOGIN_REQUEST) {
//						Login_Request req = Login_Request.read(is);
//					}
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}
		}

	}

	public void setOutput(Message msg) {
//		synchronized (output) {
			output.add(msg);
//		}
	}

	void writeMessage(Message msg) {
		if (msg instanceof Login_Request) {
			Login_Request req = (Login_Request) msg;
			try {
				req.write(os);
				System.out.println("Message sent");
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

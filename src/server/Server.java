package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import core.Login_Request;
import core.Message;

public class Server {
	
	ExecutorService pool;
	ServerSocket serverSocket;
	
	public static void main(String[] args) {
		Server s = null;
		try {
			s =  new Server();
			while(true) {
				s.listen();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				s.serverSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public Server() throws UnknownHostException, IOException {
		// Initialize a server socket
		pool = Executors.newCachedThreadPool();
		serverSocket = new ServerSocket(3000);
		System.out.println("Waiting for a client ");
		

	}

	public void listen() throws IOException {
		Socket socket = serverSocket.accept();
		Connection con = new Connection(socket);
		pool.execute(con);
		
	}
	
}
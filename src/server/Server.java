package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
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
		
			e.printStackTrace();
		}finally {
			try {
				s.serverSocket.close();
			} catch (IOException e) {
			
				e.printStackTrace();
			}
		}
	}

	public Server() throws UnknownHostException, IOException {
		// Initialize a server socket
		pool = Executors.newCachedThreadPool();
		serverSocket = new ServerSocket(3011);
		System.out.println("Waiting for a client ");
		InetAddress localhost = InetAddress.getLocalHost(); 
	    System.out.println("System IP Address : " + 
	                      (localhost.getHostAddress()).trim()); 
		

	}

	public void listen() throws IOException {
		Socket socket = serverSocket.accept();
		Connection con = new Connection(socket);
		pool.execute(con);
		
	}
	
}
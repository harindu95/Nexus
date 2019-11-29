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
	static int portNum = 3001;
	
	public static void main(String[] args) {
		Server s = null;
		try {
			if(args.length > 0)
			portNum = Integer.parseInt(args[0]);
		}catch(NumberFormatException e) {
			
		}
		try {
		s =  new Server(portNum);
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

	public Server(int port) throws UnknownHostException, IOException {
		
		// Initialize a server socket
		pool = Executors.newCachedThreadPool();
		serverSocket = new ServerSocket(portNum);
		InetAddress localhost = InetAddress.getLocalHost(); 
	    System.out.println("Host IP Address : " + 
	                      (localhost.getHostAddress()).trim()); 
	    System.out.println("Host port number : " + portNum); 
		System.out.println("Waiting for a client ");
		

	}
	
	public Server() throws UnknownHostException, IOException {
		// Initialize a server socket
		pool = Executors.newCachedThreadPool();
		serverSocket = new ServerSocket(3001);
		InetAddress localhost = InetAddress.getLocalHost(); 
	    System.out.println("Host IP Address : " + 
	                      (localhost.getHostAddress()).trim()); 
		System.out.println("Waiting for a client ");
		

	}

	public void listen() throws IOException {
		Socket socket = serverSocket.accept();
		Connection con = new Connection(socket);
		pool.execute(con);
		
	}
	
}
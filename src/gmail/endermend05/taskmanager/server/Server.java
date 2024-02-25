package gmail.endermend05.taskmanager.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import gmail.endermend05.taskmanager.server.api.ServerListener;

public class Server {
	int port;
	ServerSocket mainSocket;
	
	ServerListener listener;
	
	private void processClient(int id) {
		Socket socket = null;
		
		try {
			System.out.println("Server is waiting for client...");
			socket = this.mainSocket.accept();
			System.out.println("Client connected...");
		} catch (IOException e) {
			System.out.println("Client cannon connect");
			return;
		}
		
		new Thread( new ServerThread(socket, listener)).start();   
		
	}
	
	public Server(int port, ServerListener listener) {
		this.listener = listener;
		this.port = port;
		
		try {
			mainSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("Socket cannot be created");
			System.exit(1);
		}
		
		System.out.println("Socket created successfuly");	
		
		int id = 0;
		while (true) {
			processClient(id);
			id++;
		}
		
	}
}

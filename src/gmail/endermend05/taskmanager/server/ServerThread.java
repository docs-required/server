package gmail.endermend05.taskmanager.server;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gmail.endermend05.taskmanager.server.api.ServerListener;
import gmail.endermend05.taskmanager.server.impl.ServerStrategy;

public class ServerThread implements Runnable{
	
	private static final Pattern COMMAND_PATTERN = Pattern.compile("(?<action>[a-zA-z]+):\\{(?<additionalinfo>[^\\}]+)\\}");
	
	private ServerListener listener;
	private Socket socket;
	public ServerThread(Socket socket, ServerListener listener) {
		this.socket = socket;
		this.listener = listener;
	}
	private void processSocket(Socket socket) {

		try {
			BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
			OutputStream out = socket.getOutputStream();

			PrintWriter output = new PrintWriter(out, true);

			processInputOutput(in, output);

		} catch (IOException e) {
			System.out.println("�� ������� �������� ������");

		}

	}
	private String processCommand(String input) {
		if(input.startsWith("commands")) return String.join("\n",ServerStrategy.ACTIONS.Commands());
		if(input.startsWith("disconnect")) return "disconnect";
		Matcher command =  COMMAND_PATTERN.matcher(input);
		if(!command.matches()) {
			System.out.println("Wrong command");
			return "wrong command";
		}
		return listener.onRecieveCommand(input);

	}
	
	private void processInputOutput(BufferedInputStream input, PrintWriter output) {
		try {
			while (true) {
				
				byte[] buffer = new byte[1024];
				int read;
				String text = "";
				while((read = input.read(buffer)) != -1) {
					text+=new String(buffer, 0, read);
				    if(read<1024) break;
				}
				text.substring(0, text.length()-1);
				System.out.println("Input:"+text);
				String response = processCommand(text);
				System.out.println("Responce:" + (response!=null? response:"[NULL]"));
				if (response.equals("disconnect")) {
					break;
				}
				
				output.println(response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public void run() {
		processSocket(socket);

		try {
			socket.close();
		} catch (IOException e) {
			System.out.println("Cannot close socket");
		}

		System.out.println("Socket closed...");
	}
}

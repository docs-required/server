package gmail.endermend05.taskmanager;

import gmail.endermend05.taskmanager.server.Server;
import gmail.endermend05.taskmanager.server.api.ServerListener;
import gmail.endermend05.taskmanager.server.impl.ServerStrategy;

public class TaskManager {
	public static void main(String args[]) {
		ServerListener listener = new ServerStrategy();
		new Server(Integer.parseInt(args[0]),listener);
	}

}

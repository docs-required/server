package gmail.endermend05.taskmanager.server.impl;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import gmail.endermend05.taskmanager.server.api.ServerListener;

public class ServerStrategy implements ServerListener{
	
	private static final Pattern COMMAND_PATTERN = Pattern.compile("(?<action>[_a-zA-z]+):\\{(?<additionalinfo>[^\\}]+)\\}");
	
	private static final Pattern ID = Pattern.compile("[^-?\\d+$]+");
	private static final Pattern CLIENT_ID = ID, TEAM_ID = ID, TASK_ID = ID;
	private static final Pattern PASSWORD = Pattern.compile("[^-?\\d+$]+");
	private static final Pattern NAME = Pattern.compile("[\\p{IsAlphabetic}0-9\\.-]+");
	private static final Pattern DESCRIPTION = Pattern.compile("[\\p{IsAlphabetic}\\p{Punct}\\p{Space}\\n]+");

	private static final String W_ADD_INFO = "wrong additional info";
	
	
	private static Map<Integer,Team> team_pool;
	private static Map<Integer,Client> client_pool;
	static {
		client_pool = new HashMap<Integer, Client>();
		team_pool = new HashMap<Integer, Team>();
	}

	
	@Override
	public String onRecieveCommand(String input) {
		Matcher command = COMMAND_PATTERN.matcher(input);
		if(!command.matches()) return "wrong command";
		return ACTIONS.run(command.group("action"), command.group("additionalinfo").split(";"));
	}

	private static final Function<String[],String> register = info->{
		int id = Integer.parseInt(info[0]);
		if(client_pool.containsKey(id))return "false";
		String name = info[1];
		String description = info[2];
		int password = Integer.parseInt(info[3]);
		client_pool.put(id, new Client(id,name,description,password));
		return "true";
	};
	
	private static final Function<String[], String> login = info ->{
		int id = Integer.parseInt(info[0]);
		int password = Integer.parseInt(info[1]);
		Client client = client_pool.get(id);
		if(client == null) return W_ADD_INFO;
		return Boolean.toString(client.PasswordCheck(password));
	};
	
	private static final Function<String[], String> leave = info ->{
		int id = Integer.parseInt(info[0]);
		Client client = client_pool.get(id);
		if(client == null) return W_ADD_INFO;
		client.setState(gmail.endermend05.taskmanager.server.api.Client.State.OFFLINE);
		return "";
	};
	
	private static final Function<String[], String> get_state = info ->{
		int id = Integer.parseInt(info[0]);
		Client client = client_pool.get(id);
		if(client == null) return W_ADD_INFO;
		return client.getState().toString();
	};
	
	private static final Function<String[], String> client_get_name = info ->{
		int id = Integer.parseInt(info[0]);
		Client client = client_pool.get(id);
		if(client == null) return W_ADD_INFO;
		return client.getName();
	};
	
	private static final Function<String[], String> client_set_name = info ->{
		int id = Integer.parseInt(info[0]);
		Client client = client_pool.get(id);
		if(client == null) return W_ADD_INFO;
		client.setName(info[1]);
		return "";
	};
	
	private static final Function<String[], String> client_get_description = info ->{
		int id = Integer.parseInt(info[0]);
		Client client = client_pool.get(id);
		if(client == null) return W_ADD_INFO;
		return String.join("\n", client.getDescription());
	};
	
	private static final Function<String[], String> client_set_description = info ->{
		int id = Integer.parseInt(info[0]);
		Client client = client_pool.get(id);
		if(client == null) return W_ADD_INFO;
		client.setDescription(info[1]);
		return "";
	};
	
	private static final Function<String[], String> client_add_team = info ->{
		int C_id = Integer.parseInt(info[0]);
		Client client = client_pool.get(C_id);
		if(client == null) return W_ADD_INFO;
		int T_id = Integer.parseInt(info[1]);
		Team team = team_pool.get(T_id);
		if(team == null) return W_ADD_INFO;
		team.ClientJoin(client);
		return "";
	};
	
	private static final Function<String[], String> client_remove_team = info ->{
		int C_id = Integer.parseInt(info[0]);
		Client client = client_pool.get(C_id);
		if(client == null) return W_ADD_INFO;
		int T_id = Integer.parseInt(info[1]);
		Team team = team_pool.get(T_id);
		if(team == null) return W_ADD_INFO;
		team.ClientLeave(client.getId());
		return "";
	};
	
	private static final Function<String[], String> client_get_teams = info ->{
		int C_id = Integer.parseInt(info[0]);
		Client client = client_pool.get(C_id);
		if(client == null) return W_ADD_INFO;
		return String.join(",", client.getTeamsId());
	};
	
	private static final Function<String[], String> client_get_tasks = info ->{
		int C_id = Integer.parseInt(info[0]);
		Client client = client_pool.get(C_id);
		if(client == null) return W_ADD_INFO;
		return String.join(",",client.getActiveTickets().stream().map(t->Integer.toString(t.getId())).collect(Collectors.toList()));
	};
	
	private static final Function<String[], String> client_add_task = info ->{
		int C_id = Integer.parseInt(info[0]);
		Client client = client_pool.get(C_id);
		if(client == null) return W_ADD_INFO;
		int team_id = Integer.parseInt(info[1]);
		Team team = team_pool.get(team_id);
		if(team == null) return W_ADD_INFO;
		int task_id = Integer.parseInt(info[2]);
		team.ClientBeginTicket(client, task_id);
		return "";
	};
	
	private static final Function<String[], String> client_remove_task = info ->{
		int C_id = Integer.parseInt(info[0]);
		Client client = client_pool.get(C_id);
		if(client == null) return W_ADD_INFO;
		int team_id = Integer.parseInt(info[1]);
		Team team = team_pool.get(team_id);
		if(team == null) return W_ADD_INFO;
		int task_id = Integer.parseInt(info[2]);
		team.FinishTicket(task_id);
		client.removeTicket(team.getTicket(task_id));
		return "";
	};
	
	private static final Function<String[], String> team_create = info ->{
		int id = Integer.parseInt(info[0]);
		if(team_pool.containsKey(id)) return "false";
		team_pool.put(id, new Team(info[1], id, Arrays.asList(info[2].split("\n"))));
		return "true";
	};
	
	private static final Function<String[], String> team_get_name = info ->{
		int id = Integer.parseInt(info[0]);
		Team team = team_pool.get(id);
		if(team == null) return W_ADD_INFO;
		return team.getName();
	};
	
	private static final Function<String[], String> team_set_name = info ->{
		int id = Integer.parseInt(info[0]);
		Team team = team_pool.get(id);
		if(team == null) return W_ADD_INFO;
		team.setName(info[1]);
		return "";
	};
	
	private static final Function<String[], String> team_get_description = info ->{
		int id = Integer.parseInt(info[0]);
		Team team = team_pool.get(id);
		if(team == null) return W_ADD_INFO;
		return String.join("\n", team.getDescription());
	};
	
	private static final Function<String[], String> team_set_description = info ->{
		int id = Integer.parseInt(info[0]);
		Team team = team_pool.get(id);
		if(team == null) return W_ADD_INFO;
		team.setDescription(info[1]);
		return "";
	};
	
	private static final Function<String[], String> team_add_client = info ->{
		int T_id = Integer.parseInt(info[0]);
		Team team = team_pool.get(T_id);
		if(team == null) return W_ADD_INFO;
		int C_id = Integer.parseInt(info[1]);
		Client client = client_pool.get(C_id);
		if(client == null) return W_ADD_INFO;
		team.ClientJoin(client);
		return "";
	};
	
	private static final Function<String[], String> team_remove_client = info ->{
		int T_id = Integer.parseInt(info[0]);
		Team team = team_pool.get(T_id);
		if(team == null) return W_ADD_INFO;
		int C_id = Integer.parseInt(info[1]);
		Client client = client_pool.get(C_id);
		if(client == null) return W_ADD_INFO;
		team.ClientLeave(client.getId());
		return "";
	};
	
	private static final Function<String[], String> team_get_clients = info ->{
		int T_id = Integer.parseInt(info[0]);
		Team team = team_pool.get(T_id);
		if(team == null) return W_ADD_INFO;
		return String.join(",", team.getClientsId().stream().map(id->id.toString()).collect(Collectors.toList()));
	};
	
	private static final Function<String[], String> team_add_ticket = info ->{
		int team_id = Integer.parseInt(info[0]);
		Team team = team_pool.get(team_id);
		if(team == null) return W_ADD_INFO;
		int task_id = Integer.parseInt(info[1]);
		if(team.doesTicketExists(task_id)) return "false";
		Ticket task = new Ticket(info[2],Arrays.asList(info[3].split("\n")),task_id);
		team.CreateTicket(task);
		return "true";
	};
	
	public static enum ACTIONS {
		//Client
		REGISTER("client_register", register, CLIENT_ID, NAME, DESCRIPTION, PASSWORD),
		LOGIN("client_login", login, CLIENT_ID, PASSWORD),
		LEAVE("client_leave", leave, CLIENT_ID),
		GETSTATE("client_get_state", get_state, CLIENT_ID),
		CLIENTGETNAME("client_get_name", client_get_name, CLIENT_ID),
		CLIENTGETDESCRIPTION("client_get_description", client_get_description, CLIENT_ID),
		CLIENTSETNAME("client_change_name", client_set_name, CLIENT_ID, NAME),
		CLIENTSETDESCRIPTION("client_change_description", client_set_description, CLIENT_ID, DESCRIPTION),
		CLIENTADDTEAM("client_add_team", client_add_team, CLIENT_ID, TEAM_ID),
		CLIENTREMOVETEAM("client_remove_team", client_remove_team, CLIENT_ID, TEAM_ID),
		CLIENTGETTEAMSID("client_get_teamsID", client_get_teams, CLIENT_ID),
		CLIENTGETTASKS("client_get_tasks", client_get_tasks, CLIENT_ID),
		CLIENTADDTASK("client_start_task", client_add_task, CLIENT_ID, TEAM_ID, TASK_ID),
		CLIENTREMOVETASK("client_finish_task",client_remove_task, CLIENT_ID, TEAM_ID, TASK_ID),
		//Team
		TEAMCREATE("team_create", team_create, TEAM_ID, NAME, DESCRIPTION),
		TEAMGETNAME("team_get_name", team_get_name, TEAM_ID),
		TEAMGETDESCRIPTION("team_get_description", team_get_description, TEAM_ID),
		TEAMSETNAME("team_change_name", team_set_name, TEAM_ID, NAME),
		TEAMSETDESCRIPTION("team_change_description", team_set_description, TEAM_ID, DESCRIPTION),
		TEAMADDCLIENT("team_add_client", team_add_client, TEAM_ID, CLIENT_ID),
		TEAMREMOVECLIENT("team_remove_client", team_remove_client, TEAM_ID, CLIENT_ID),
		TEAMGETCLIENTSID("team_get_clientsID",team_get_clients, TEAM_ID),
		TEAMADDTICKET("team_add_ticket",team_add_ticket, TEAM_ID, TASK_ID, NAME, DESCRIPTION);
		private static final String W_ACTION = "wrong action";
		private static final String W_ADD_INFO = "wrong addtional info";
		
		private final String action;
		private final Pattern[] patterns;
		private final Function<String[],String> function;
		
		ACTIONS(String action,Function<String[],String> function, Pattern... patterns ) {
			this.action = action;
			this.patterns = patterns.clone();
			this.function = function;
		}
		
		private String process(String... info) {
			if(info.length != patterns.length) {
				return W_ADD_INFO;
			}
			for(int i = 0; i < info.length; i++) {
				if(info[i].isEmpty() || !patterns[i].matcher(info[i]).matches()) {
					return W_ADD_INFO;
				}
			}
			return function.apply(info);
		}
		
		private static Optional<ACTIONS> resolveAction(String action) {
			return Arrays.stream(values()).filter(a->a.action.equals(action)).findAny();
		}
		
		public static String run(String action, String[] info) {
			return resolveAction(action).map(a->a.process(info)).orElse(W_ACTION);
		}
		
		public static String[] Commands() {
			return Arrays.stream(values()).map(c->{
				String patterns = String.join(";", Arrays.stream(c.patterns).map(p->p.toString()).collect(Collectors.toList()));
				return c.action + ":{" + patterns + "}";
			}).toArray(String[] :: new);
		}
	}
}

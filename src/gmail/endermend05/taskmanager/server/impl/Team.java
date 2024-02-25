package gmail.endermend05.taskmanager.server.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gmail.endermend05.taskmanager.server.api.Client;
import gmail.endermend05.taskmanager.server.api.TeamListener;
import gmail.endermend05.taskmanager.server.api.Ticket;

public class Team implements TeamListener{
	
	private Map<Integer,Client> team;
	private Map<Integer,Ticket> tickets;
	private String name;
	private List<String> description;
	private int id;
	
	public Team(String name, int id, List<String> description) {
		this.name = name;
		this.id = id;
		this.description = description;
		team = new HashMap<Integer, Client>();
		tickets =  new HashMap<Integer, Ticket>();
	}
	
	@Override
	public String onRecieveCommand(String command) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void CreateTicket(Ticket ticket) {
		tickets.put(ticket.getId(),ticket);
	}
	
	@Override
	public boolean doesTicketExists(int ticketId) {
		return tickets.containsKey(ticketId);
	}
	
	@Override
	public void ClientBeginTicket(Client client, int ticketId) {
		if(!team.containsValue(client)) {
			System.out.println("Client is not in team");
			return;
		}
		Ticket ticket = tickets.get(ticketId);
		if(ticket == null) {
			System.out.println("There is no such task in team");
			return;
		}
		ticket.setExecutor(client);
		client.addNewTicket(ticket);
	}

	@Override
	public void FinishTicket(int ticketId) {
		Ticket ticket = tickets.get(ticketId);
		if(ticket == null) {
			System.out.println("There is no such task in team");
			return;
		}
		ticket.finish();
	}
	
	@Override
	public void CloseTicket(int ticketId) {
		Ticket ticket = tickets.get(ticketId);
		if(ticket == null) {
			System.out.println("There is no such task in team");
			return;
		}
		ticket.close();		
	}
	
	@Override
	public void DeleteTicket(int ticketId) {
		Ticket ticket = tickets.get(ticketId);
		if(ticket == null) {
			System.out.println("There is no such task in team");
			return;
		}
		ticket.archived();
	}

	@Override
	public void ClientJoin(Client client) {
		team.put(client.getId(), client);
		client.addTeam(this);
	}
	
	@Override
	public void ClientLeave(int clientId) {
		team.get(clientId).removeTeam(this);
		team.remove(clientId);
	}

	@Override
	public Ticket getTicket(int ticketId) {
		return tickets.get(ticketId);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<String> getDescription() {
		return description;
	}
	
	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setDescription(String description) {
		this.description = Arrays.asList(description.split("\n"));
	}
	
	@Override
	public Set<Integer> getClientsId() {
		return this.team.keySet();
	}
}

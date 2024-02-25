package gmail.endermend05.taskmanager.server.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import gmail.endermend05.taskmanager.server.api.Ticket;
import gmail.endermend05.taskmanager.server.api.TicketState;

public class Client implements gmail.endermend05.taskmanager.server.api.Client{
	public static long afk_time = 15*60*1000;
	private int id;
	private String name;
	private int password;
	private List<String> description;
	private List<Team> teams;
	private List<Ticket> tickets;
	private gmail.endermend05.taskmanager.server.api.Client.State state;
	private long lastActivity;
	
	public Client(int id, String name, int password) {
		this.id = id;
		this.name = name;
		this.password = password;
		this.state = gmail.endermend05.taskmanager.server.api.Client.State.ONLINE;
		teams = new ArrayList<Team>();
		tickets = new ArrayList<Ticket>();
		load_tickets();
		description = new ArrayList<String>();
	}
	
	
	public Client(int id, String name, String description, int password) {
		this.id = id;
		this.name = name;
		this.password = password;
		this.state = gmail.endermend05.taskmanager.server.api.Client.State.ONLINE;
		tickets = new ArrayList<Ticket>();
		teams = new ArrayList<Team>();
		load_tickets();
		this.description = Arrays.asList(description.split("\n"));
	}
	
	@Override
	public void removeTeam(Team team) {
		teams.remove(team);
	}
	
	@Override
	public void addTeam(Team team) {
		teams.add(team);
	}
	
	public List<String> getTeamsId(){
		return teams.stream().map(t->Integer.toString(t.getId())).collect(Collectors.toList());
	}
	
	public void updateActivity() {
		lastActivity = Calendar.getInstance().getTimeInMillis();
	}
	
	public boolean isAFK() {
		return Calendar.getInstance().getTimeInMillis() - lastActivity > afk_time;
	}
	
	public void setState(gmail.endermend05.taskmanager.server.api.Client.State state) {
		this.state = state;
	}
	
	public gmail.endermend05.taskmanager.server.api.Client.State getState(){
		return this.state;
	}
	
	public boolean PasswordCheck(int password) {
		return this.password == password;
	}
	
	private void load_tickets() {
		//TODO load saved tickets
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public List<Ticket> getActiveTickets() {
		return tickets.stream()
				.filter(t->t.getState().equals(TicketState.ACTIVE))
				.collect(Collectors.toList());
	}

	@Override
	public void addNewTicket(Ticket ticket) {
		tickets.add(ticket);
	}
	
	@Override
	public void removeTicket(Ticket ticket) {
		tickets.remove(ticket);
	}

	@Override
	public List<String> getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = Arrays.asList(description.split("\n"));
	}
}

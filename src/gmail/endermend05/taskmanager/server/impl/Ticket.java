package gmail.endermend05.taskmanager.server.impl;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import gmail.endermend05.taskmanager.server.api.Client;
import gmail.endermend05.taskmanager.server.api.TicketState;
import gmail.endermend05.taskmanager.server.api.TicketType;

public class Ticket implements gmail.endermend05.taskmanager.server.api.Ticket{
	private Client executor;
	private String topic;
	private List<String> description;
	private TicketType type;
	private TicketState state = TicketState.OPEN;
	private int id;
	private long creation_date_ms, begin_executuin_date_ms;
	
	public Ticket(String topic, List<String> description, int id) {
		this.topic = topic;
		this.description = description;
		this.id = id;
		creation_date_ms = Calendar.getInstance().getTimeInMillis();
	}
	
	@Override
	public String onRecieveCommand(String command) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Client> getExecutor() {
		return Optional.ofNullable(executor);
	}

	@Override
	public String getTopic() {
		return topic;
	}

	@Override
	public List<String> getDescription() {
		return description;
	}

	@Override
	public TicketType getType() {
		return type;
	}

	@Override
	public TicketState getState() {
		return state;
	}

	@Override
	public long timeFromCreationMs() {
		return Calendar.getInstance().getTimeInMillis() - creation_date_ms;
	}

	@Override
	public void setExecutor(Client client) {
		this.executor = client;
		this.state = TicketState.ACTIVE;
		begin_executuin_date_ms = Calendar.getInstance().getTimeInMillis();
	}

	@Override
	public long timeFromBeginExecuting() {
		return Calendar.getInstance().getTimeInMillis() - begin_executuin_date_ms;
	}
	
	@Override
	public void finish() {
		this.state = TicketState.FINISHED;
	}
	
	@Override
	public void close() {
		this.state = TicketState.CLOSED;
	}
	
	public void archived() {
		this.state = TicketState.ARCHIVED;
	}

	@Override
	public String getName() {
		return getTopic();
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setName(String name) {
		this.topic = name;
	}

	@Override
	public void setDescription(String description) {
		this.description = Arrays.asList(description.split("\n"));
	}
}

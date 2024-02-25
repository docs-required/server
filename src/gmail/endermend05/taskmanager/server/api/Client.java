package gmail.endermend05.taskmanager.server.api;

import java.util.List;

import gmail.endermend05.taskmanager.server.impl.Team;

public interface Client extends Identifiable{
	public static enum State{
		ONLINE, OFFLINE, AFK;
	}
	/**
	 * Returns client display name
	 * @return client name
	 */
	public String getName();
	
	/**
	 * Sets client's name
	 * @param name - new name
	 */
	public void setName(String name);
	
	/**
	 * Returns client private id
	 * @return client id
	 */
	public int getId();
	
	/**
	 * Returns client active tickets
	 * @return active tickets
	 */
	public List<Ticket> getActiveTickets();
	
	/**
	 * Add new active ticket to the client
	 * @param ticket
	 */
	public void addNewTicket(Ticket ticket);
	
	/**
	 * Remove a ticket
	 * @param ticket
	 */
	void removeTicket(Ticket ticket);

	void removeTeam(Team team);

	void addTeam(Team team);
}

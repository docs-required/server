package gmail.endermend05.taskmanager.server.api;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface TeamListener extends CommandListener, Identifiable{
	/**
	 * Fires, when client create a new ticket
	 * @param ticket - created ticket
	 */
	public void CreateTicket(Ticket ticket);
	
	/**
	 * Fires, when client start working on the ticket
	 * @param client - client, started working on the ticket
	 * @param ticketId - ticket, client started on
	 */
	public void ClientBeginTicket(Client client, int ticketId);
	
	/**
	 * Fires, when client finish working on the ticket
	 * @param ticketId - ticket, client finished working on
	 */
	public void FinishTicket(int ticketId);
	
	/**
	 * Fires, when client close the ticket
	 * @param ticketId - closed ticket
	 */
	public void CloseTicket(int ticketId);
	
	/**
	 * Fires, when client delete the ticket
	 * @param ticketId - deleted ticket
	 */
	public void DeleteTicket(int ticketId);
	
	/**
	 * Fires, when client join the team
	 * @param client - joined client
	 */
	public void ClientJoin(Client client);
	
	/**
	 * Fires, when client leave the team
	 * @param clientId - left client
	 */
	public void ClientLeave(int clientId);

	/**
	 * Get ticket by ticketId
	 * @param ticketId
	 * @return team's ticket
	 */
	public Ticket getTicket(int ticketId);

	public Set<Integer> getClientsId();

	boolean doesTicketExists(int ticketId);
}

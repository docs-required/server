package gmail.endermend05.taskmanager.server.api;

import java.util.Optional;

public interface Ticket extends CommandListener, Identifiable{
	public Optional<Client> getExecutor();
	public String getTopic();
	public TicketType getType();
	public TicketState getState();
	public long timeFromCreationMs();
	
	public void setExecutor(Client client);
	public long timeFromBeginExecuting();
	void finish();
	void close();
	void archived();
}

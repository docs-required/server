package gmail.endermend05.taskmanager.server.api;

import java.util.List;

public interface Identifiable {
	/**
	 * @return public name
	 */
	public String getName();
	
	/**
	 * @return public description
	 */
	public List<String> getDescription();
	
	/**
	 * @return private id
	 */
	public int getId();
	
	public void setName(String name);
	
	public void setDescription(String description);
}

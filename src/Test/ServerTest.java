package Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import gmail.endermend05.taskmanager.server.api.ServerListener;
import gmail.endermend05.taskmanager.server.impl.ServerStrategy;

public class ServerTest {
	
	private static final String W_ACTION = "wrong action";
	private static final String W_ADD_INFO = "wrong addtional info";
	
	private static ServerListener listener;
	static {
		listener = new ServerStrategy();
	}
	private String send(String command) {
		return listener.onRecieveCommand(command);
	}
	@Test
	public void register() {
		assertEquals("true", send("client_register:{10101;Denis;Loves;12312}"));
		assertEquals("false", send("client_register:{10101;Pavel;Hates;99123}"));
	}
	
	@Test
	public void registerFailureAction() {
		assertAll("Should ",
			() -> assertEquals(W_ACTION, send("client_registe:{99999;Denis;Loves;12312}")),
			() -> assertEquals(W_ADD_INFO, send("client_register:{99999f;Denis;Loves;12312}")),	
			() -> assertEquals(W_ADD_INFO, send("client_register:{99999;;12312}"))
		);
	}
	
	@Test
	public void teamCreate() {
		assertEquals("true", send("team_create:{20202;BESTTEAM;Likes Cookies}"));
		assertEquals("false", send("team_create:{20202;BESTEAM;Likes Pongo}"));
	}
	
}

package zone.wim.library;

import java.net.*;
import java.nio.file.*;
import org.junit.jupiter.api.*;

import java.util.logging.*;
import zone.wim.item.*;
import zone.wim.library.*;
import zone.wim.exception.ItemException.*;

public class JpaStoreTest {
	static Logger LOGGER = Logger.getLogger(JpaStoreTest.class.getCanonicalName());
	static Path PATH;
	static InetAddress HOST;
	static JpaStore store;
	
	@BeforeAll
	static void setupPath() {
		try {
			HOST = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		store = new JpaStore();
	}
	
	@Test
	void persistAndRetrieve() {
		persistItemToStore();
		retrieveItemFromStore();
	}
	
	void persistItemToStore() {
		
		store.open();
		Host hostItem;
		hostItem = Host.create(HOST);
		store.put(hostItem);
			
		assert (store.contains(hostItem));

		store.close();
			
	}
	
	void retrieveItemFromStore() {
		
		store.open();
		Host hostItem = null;
		try {
			hostItem = (Host) store.get(HOST.getHostAddress());
		} catch (NotFound e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LOGGER.info("hostItem: " + hostItem);
		assert (hostItem instanceof Host);
		
	}
}

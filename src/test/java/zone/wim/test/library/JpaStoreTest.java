package zone.wim.test.library;

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
		HostItem hostItem;
		try {
			hostItem = new HostItem(HOST);
			store.put(hostItem);
			store.close();
			
		} catch (SignersOnly e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	void retrieveItemFromStore() {
		
		store.open();
		HostItem hostItem = null;
		try {
			hostItem = (HostItem) store.get(HOST.getHostAddress());
		} catch (NotFound e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LOGGER.info("hostItem: " + hostItem);
		assert (hostItem instanceof HostItem);
		
	}
}

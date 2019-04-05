package zone.wim.library;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.*;
import java.util.logging.Logger;

import org.junit.jupiter.api.*;

import zone.wim.exception.ItemException.NotFound;
import zone.wim.exception.ItemException.SignersOnly;
import zone.wim.item.*;
import zone.wim.library.*;

public class JdoStoreTest {
	static Logger LOGGER = Logger.getLogger(JdoStoreTest.class.getCanonicalName());
	static Path PATH;
	static InetAddress HOST;
	static JdoStore store;

	@BeforeAll
	static void setupPath() {
		PATH = Paths.get(System.getProperty("user.dir"));
		try {
			HOST = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		store = new JdoStore();
	}

	@Test
	void persistAndRetrieve() {
		persistItemToStore();
		retrieveItemFromStore();
	}
	
	void persistItemToStore() {

		HostItem hostItem;
		store.open();
		
		try {
			hostItem = new HostItem(HOST);
			store.put(hostItem);
			store.close();
		} catch (SignersOnly e) {
			e.printStackTrace();
		}
		
	}
	
	void retrieveItemFromStore() {
		
		store.open();
		
		HostItem hostItem = null;
		try {
			hostItem = (HostItem) store.get(HOST.getHostAddress());
		} catch (NotFound e) {
			e.printStackTrace();
		}
		
		assert (hostItem instanceof HostItem);
	}
}

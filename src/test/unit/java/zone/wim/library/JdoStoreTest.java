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
	static Store store;

	@BeforeAll
	static void setupPath() {
		PATH = Paths.get(System.getProperty("user.dir"));
		try {
			HOST = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		store = new Store();
	}

	@Test
	void persistAndRetrieve() {
		persistItemToStore();
		retrieveItemFromStore();
	}
	
	void persistItemToStore() {

		Item item = new TestItem("@test.item");
		store.open();
		
		store.put(item);
		store.close();
		
	}
	
	void retrieveItemFromStore() {
		
		store.open();

		Item item = null;
		try {
			item = store.get("@test.item");
		} catch (NotFound e) {
			e.printStackTrace();
		}
		
		assert (item instanceof Item);
		System.out.println("ITEM! " + item.toString());
	}
}

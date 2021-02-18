package zone.wim.library;

import java.util.logging.Logger;


import org.junit.jupiter.api.*;

import zone.wim.exception.StoreException.NotFound;
import zone.wim.item.*;
import zone.wim.library.store.ItemStore;

public class JdoStoreTest {
	static Logger LOGGER = Logger.getLogger(JdoStoreTest.class.getCanonicalName());
	static ItemStore store;
	static String path = "data/test.odb";

	@BeforeAll
	static void setupPath() {
		store = new ItemStore(path);
	}

	@Test
	void persistAndRetrieve() {
		persistItemToStore();
		retrieveItemFromStore();
	}
	
	void persistItemToStore() {
		store.open();

		Item item = new TestItem("@test.item");
		System.out.println("ITEM: " + item.toString());
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

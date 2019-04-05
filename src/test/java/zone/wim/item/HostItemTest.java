package zone.wim.item;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import org.junit.jupiter.api.*;

import zone.wim.exception.ItemException.SignersOnly;
import zone.wim.item.*;

public class HostItemTest {
	static Logger LOGGER = Logger.getLogger(HostItemTest.class.getCanonicalName());
	@Test
	void testConstructor() {
		InetAddress localhost;
		try {
			localhost = InetAddress.getLocalHost();
			HostItem hostItem = new HostItem(localhost);
			LOGGER.info(localhost.toString());
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SignersOnly e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

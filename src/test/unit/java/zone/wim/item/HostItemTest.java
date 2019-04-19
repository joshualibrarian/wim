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
	void creationTest() {
		InetAddress localhost = InetAddress.getLoopbackAddress();
		Host hostItem = Host.create(localhost);
		assert (hostItem instanceof Host);
		LOGGER.info(localhost.toString());
	}
}

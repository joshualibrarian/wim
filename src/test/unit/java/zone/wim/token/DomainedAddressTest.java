package zone.wim.token;

import org.junit.jupiter.api.*;

import java.util.logging.Logger;
import zone.wim.item.*;
import zone.wim.token.DomainedAddress;
import zone.wim.exception.*;

public class DomainedAddressTest {
	static Logger LOGGER = Logger.getLogger(DomainedAddressTest.class.getCanonicalName());
	
	@Test
	void RegexTest() {
		try {
			LOGGER.info(DomainedAddress.Regex.COMPLETE);
			DomainedAddress address = DomainedAddress.parse("@test.net");
			assert (address instanceof DomainedAddress);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

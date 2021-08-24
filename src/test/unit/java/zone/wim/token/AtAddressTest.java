package zone.wim.token;

import org.junit.jupiter.api.*;

import java.util.logging.Logger;

import zone.wim.item.tokens.AtDomainAddress;

public class AtAddressTest {
	static Logger LOGGER = Logger.getLogger(AtAddressTest.class.getCanonicalName());
	
	@Test
	void RegexTest() {
		try {
			LOGGER.info(AtDomainAddress.Regex.COMPLETE);
			AtDomainAddress address = AtDomainAddress.parse("@test.net");
			assert (address instanceof AtDomainAddress);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

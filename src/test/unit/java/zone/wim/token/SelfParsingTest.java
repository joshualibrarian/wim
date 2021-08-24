package zone.wim.token;

import org.junit.jupiter.api.*;
import zone.wim.coding.SelfCoding;
import zone.wim.item.tokens.AtDomainAddress;
import zone.wim.token.SelfCodingException.*;

public class SelfParsingTest {

	@Test
	void parseTestIsDomain() {
		try {
			SelfCoding token = SelfCoding.decode("@test.net");
			assert (token instanceof AtDomainAddress);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void parseTestIsHost() {
		try {
			SelfCoding token = SelfCoding.decode("192.168.0.1");
			assert (token instanceof HostAddress);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	@Test
	void parseTestIsUnknown() {
		try {
			SelfCoding.decode("INVALID TOKEN!");
		} catch (Exception e) {
			assert (e instanceof Unknown);
		}
	}

}

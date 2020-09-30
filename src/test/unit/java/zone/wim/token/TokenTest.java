package zone.wim.token;

import java.util.*;

import org.junit.jupiter.api.*;
import zone.wim.item.*;
import zone.wim.token.Token;
import zone.wim.token.TokenException.*;
import zone.wim.exception.*;

public class TokenTest {

	@Test
	void parseTestIsDomain() {
		try {
			Token token = Token.parse("@test.net");
			assert (token instanceof AtDomainAddress);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void parseTestIsHost() {
		try {
			Token token = Token.parse("192.168.0.1");
			assert (token instanceof HostAddress);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	@Test
	void parseTestIsUnknown() {
		try {
			Token.parse("INVALID TOKEN!");
		} catch (Exception e) {
			assert (e instanceof Unknown);
		}
	}

}

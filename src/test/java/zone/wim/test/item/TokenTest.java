package zone.wim.test.item;

import java.util.*;
import org.junit.jupiter.api.*;
import zone.wim.item.*;
import zone.wim.token.Token;
import zone.wim.exception.*;

public class TokenTest {

	@Test
	void parseTestIsDomain() {
		try {
			List<Token> tokens = Token.parse("@test.net");
			assert (!tokens.isEmpty());
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void parseTestIsHost() {
		try {
			List<Token> tokens = Token.parse("192.168.0.1");
			assert (!tokens.isEmpty());
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}	
	
	@Test
	void parseTestIsUnknown() {
		try {
			Token.parse("INVALID TOKEN!");
		} catch (Throwable e) {
			assert (e instanceof TokenException);
		}
	}

}

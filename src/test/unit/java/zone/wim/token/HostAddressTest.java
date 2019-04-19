package zone.wim.token;

import org.junit.jupiter.api.Test;

import zone.wim.token.HostAddress;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.platform.commons.util.Preconditions.*;


public class HostAddressTest {
	
	@Test
	void parseTest() {
		HostAddress hostAddress = null;
		try {
			hostAddress = HostAddress.parse("127.0.0.1");
		} catch (Throwable e) {
			// TODO test the throws
		}
		
		assert (hostAddress instanceof HostAddress);
		
	}
	
	@Test
	void parseTestFail() {
		HostAddress hostAddress = null;
		try {
			hostAddress = HostAddress.parse("NOT AN IP!");
		} catch (Throwable e) {
			assert (e instanceof java.net.UnknownHostException);
		}
		
	}

}

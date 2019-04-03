package zone.wim.test.item;

import org.junit.jupiter.api.*;

import zone.wim.item.*;
import zone.wim.token.MediaType;
import zone.wim.exception.*;


public class MediaTypeTest {
	
	@Test
	void testParsePass() {
		try {
			MediaType token = MediaType.parse("image/jpeg");
			
			assert (token instanceof MediaType);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
	}

	@Test
	void testParseFail() {
		try {
			MediaType.parse("INVALID MIME TYPE");
			
		} catch (Throwable e) {
			assert (e instanceof TypeException.Invalid);
		}
		
	}

}

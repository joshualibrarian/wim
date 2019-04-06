package zone.wim.item;

import org.junit.jupiter.api.*;

import zone.wim.item.*;
import zone.wim.token.MediaItemType;
import zone.wim.exception.*;


public class MediaTypeTest {
	
	@Test
	void testParsePass() {
		try {
			MediaItemType token = MediaItemType.parse("image/jpeg");
			
			assert (token instanceof MediaItemType);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
	}

	@Test
	void testParseFail() {
		try {
			MediaItemType.parse("INVALID MIME TYPE");
			
		} catch (Throwable e) {
			assert (e instanceof TypeException.Invalid);
		}
		
	}

}

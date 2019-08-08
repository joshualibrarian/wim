package zone.wim.library;

import org.junit.jupiter.api.*;

import zone.wim.exception.LibraryException;
import zone.wim.exception.LibraryException.*;
import java.util.logging.Logger;

public class LibraryTest {
	static Logger LOGGER = Logger.getLogger(LibraryTest.class.getCanonicalName());
	
	@Nested
	public class Instantiation {
		
		@BeforeEach
		public void verifyInstanceIsNull() {
			Library instance = null;
			
			try {
				instance = Library.instance();
				if (instance instanceof Library) {
					instance.shutdown();
				}
			} catch (NotInitialized e) {
				LOGGER.info("instance is null in BeforeEach()");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			assert (instance == null);
		}
		
		@Test
		public void throwsExceptionIfNotInitialized() {
			Library instance = null;
			
			try {
				instance = Library.instance();
			} catch (NotInitialized e) {
				assert (e instanceof LibraryException.NotInitialized);
				assert (instance == null);
			}
		}
		
		@Test
		public void returnsInstanceIfInitialized() {
			Library instance = null;
			
			try {
				Library.main(new String[0]);
				instance = Library.instance();
			} catch(NotInitialized e) {
				e.printStackTrace();
			}
			
			assert (instance instanceof Library);
		}
	}
}

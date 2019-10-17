package zone.wim.library;

import org.junit.jupiter.api.*;

import zone.wim.exception.LibraryException;
import zone.wim.exception.LibraryException.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class LibraryTest {
	static Logger LOGGER = Logger.getLogger(LibraryTest.class.getCanonicalName());
	static String DATAFILE = "test.odb";
	
	@BeforeEach
	public void eraseLibraryData() {
		try {
			Files.deleteIfExists(Paths.get(DATAFILE));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Nested
	public class Instantiation {
		
		@Test
		public void initializationTest() {
			Library instance = null;
			
			try {
				instance = Library.instance();
			} catch (NotInitialized e) {
				assert (e instanceof LibraryException.NotInitialized);
			}

			assert (instance == null);

			Library.main(new String[] { "-l", DATAFILE });
			
			try {
				instance = Library.instance();
			} catch (NotInitialized e) {
				e.printStackTrace();
			}
			
			assert(instance instanceof Library);
		}
		
	}
}

package zone.wim.language.english;

import java.util.*;
import java.util.logging.*;
import org.junit.jupiter.api.*;

import edu.mit.jwi.item.*;
import zone.wim.item.*;
import zone.wim.language.*;
import zone.wim.token.*;

public class GeneratorTest {
	static Logger LOGGER = Logger.getLogger(GeneratorTest.class.getCanonicalName());
	static Generator generator;
	static Signer creator;
	
	@BeforeAll
	public static void initialize() {
		try {
			Address address = DomainedAddress.parse("@test.net");
			generator = new Generator(creator);
			generator.initWordNet();
			creator = new DomainedServer(address);
			assert (generator instanceof Generator);
			assert (creator instanceof DomainedServer);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
	}
	
	@Nested
	public class createLexemeTest {
		
		@Test
		public void shouldCreateNouns() {
			Iterator<ISynset> iterator = generator.wordNet.getSynsetIterator(POS.NOUN);
			
			while (iterator.hasNext()) {
				ISynset synset = iterator.next();
				assert (synset instanceof ISynset);
				LOGGER.info(synset.getID().toString());
				for (IWord word : synset.getWords()) {
					PartOfSpeech pos = PartOfSpeech.getByWordnetPOS(synset.getPOS());
					Lexeme lexeme = generator.createLexeme(pos, word);
					assert (lexeme instanceof EnglishNoun);
				}
			}
			
		}
		
	}
}

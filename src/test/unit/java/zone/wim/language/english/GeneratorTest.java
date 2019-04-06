package zone.wim.language.english;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

import java.util.logging.*;
import org.junit.jupiter.api.*;

import com.drew.lang.Charsets;

import edu.mit.jwi.item.*;
import mockit.Mocked;
import mockit.Tested;
import zone.wim.item.*;
import zone.wim.language.*;
import zone.wim.language.english.DictionaryException.NotFound;
import zone.wim.library.Library;
import zone.wim.token.*;

public class GeneratorTest {
	static Logger LOGGER = Logger.getLogger(GeneratorTest.class.getCanonicalName());

	@Tested Generator generator;
	@Mocked Library library;
	
	static Signer creator;
	
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
	
	@Nested
	public class WordnetTests {
		@Mocked Generator generator;
		
		@Test
		public void generateSynsetTestData() {
			try {
				generator.initWordnet();
				Iterator<ISynset> iterator = generator.wordNet.getSynsetIterator(POS.ADVERB);
//				List<String> 
				while(iterator.hasNext()) {
					ISynset synset = iterator.next();
					String s = synset.getID().toString() + "\n";
					Files.write(Paths.get("src/test/unit/java/resources/synset-ids.txt"), 
							s.getBytes(Charsets.UTF_8),
							StandardOpenOption.APPEND);
				}
				
			} catch (NotFound e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
	}
}

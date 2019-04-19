package zone.wim.language.english;

import java.io.IOException;

import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

import java.util.logging.*;
import org.junit.jupiter.api.*;

import com.drew.lang.Charsets;

import edu.mit.jwi.item.*;
import mockit.*;
import zone.wim.exception.ItemException.SignersOnly;
import zone.wim.item.*;
import zone.wim.language.*;
import zone.wim.language.english.DictionaryException.NotFound;
import zone.wim.library.Library;

public class GeneratorTest {
	static Logger LOGGER = Logger.getLogger(GeneratorTest.class.getCanonicalName());
	
	@Nested
	public class LexemeTests {
//		@Tested Generator generator;
		@Mocked Library library;
		
		@Test
		public void shouldCreateNouns(@Mocked Signer mockCreator) {
			Library.main(new String[0]);
			Generator generator = new Generator(mockCreator);
			try {
				generator.initWordnet();
			} catch (NotFound e) {
				e.printStackTrace();
			}
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
//		@Tested Generator generator;
		
		@Test
		public void generateSynsetTestData() {

			try {
				Library.main(new String[0]);
				InetAddress localhost = InetAddress.getLocalHost();
				Host hostItem = Host.create(localhost);
				Generator generator = new Generator(hostItem);
				generator.initWordnet();
				Iterator<ISynset> iterator = generator.wordNet.getSynsetIterator(POS.ADVERB);
//				List<String> 
				while(iterator.hasNext()) {
					ISynset synset = iterator.next();
					String s = synset.getID().toString() + "\n";
					Files.write(Paths.get("src/test/unit/java/resources/synsetid-list.txt"), 
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

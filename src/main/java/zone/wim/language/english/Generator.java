
package zone.wim.language.english;

import java.io.BufferedReader;


import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.logging.*;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.*;
import javafx.scene.layout.Pane;
import zone.wim.item.*;
import zone.wim.language.*;
import zone.wim.language.english.DictionaryException.NotFound;
import zone.wim.library.*;
import zone.wim.token.ClassItemType;
import zone.wim.token.DomainedAddress;
import zone.wim.exception.*;
import zone.wim.exception.AddressException.Invalid;
import zone.wim.exception.LibraryException.NotInitialized;

public class Generator {
	public static Logger LOGGER = Logger.getLogger(Generator.class.getCanonicalName());
	public static String DICTIONARY_PATH = "lib/english/wordnet/dict/";
	
	protected Library library;
	protected IDictionary wordNet;
	protected Signer user;
	protected Language english;

	public Generator(Signer user) {
		this.user = user;
		try {
			library = Library.instance();
		} catch (NotInitialized e1) {
			e1.printStackTrace();
		}
		
		try {
			// TODO check it if exists before creating it
			english = (Language)user.createItem("lang.english", Language.class);
		} catch (Invalid e) {
			e.printStackTrace();
		}
	}
	
	protected void initWordnet() throws NotFound {
		try { 
			URL url = new URL("file", null, DICTIONARY_PATH);
			wordNet = new Dictionary(url);
			wordNet.open();

		} catch (MalformedURLException e) {
			throw new DictionaryException.NotFound(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	protected void createSynsets(POS wnPos) {

		int count = 0;
		LOGGER.info("Creating Synsets: " + wnPos.name());
		Iterator<ISynset> synsetIterator = wordNet.getSynsetIterator(wnPos);
		
		while (synsetIterator.hasNext()) {
			count++;
			ISynset synset = synsetIterator.next();
			LOGGER.info("SYNSET: " + synset.getLexicalFile().getName() + "/" + synset.getGloss());

			Sememe sememe = createSememe(synset);
			
			for (IWord word : synset.getWords()) {
				
				PartOfSpeech pos = PartOfSpeech.getByWordnetPOS(synset.getPOS());
				Lexeme lexeme = createLexeme(pos, word);
				sememe.addLexeme(english, lexeme);

			}
		}
	}

	protected Sememe createSememe(ISynset synset) {
		Sememe sememe = null;
		int x = 0;
		int i = 0;

		while (sememe == null) {
			IWord word = synset.getWord(x);
			
			String lexicalFile = synset.getLexicalFile().getName();
			String lemma = word.getLemma();
			String path = lexicalFile + "." + word.getLemma();
			
			if (i > 0) {
				path += Integer.toString(i);
			}
			
			LOGGER.info("lemma: " + lemma);
			LOGGER.info("path: " + path);

			if (x >= synset.getWords().size()) {
				i++;
				x = 0;
			} else {
				x++;
			}
			
			try {
				sememe = (Sememe)user.createItem(path, Sememe.class);
			} catch (AddressException.Invalid e) {
				LOGGER.info("item name conflict!" + e.getMessage());
				x++;
				// proceed with iteration
			}
		}
		
		sememe.setSynsetId(synset.getID().toString());
		sememe.setGloss(english, synset.getGloss());
		
		return sememe;
	}
	
	Lexeme createLexeme(PartOfSpeech pos, IWord word) {
		Lexeme lexeme = null;
		Class<? extends Lexeme> clazz = pos.getLexemeType();
		
		try {
			Constructor<? extends Lexeme> constructor = clazz.getConstructor(String.class);
			lexeme = (Lexeme) constructor.newInstance(word.getLemma());

		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return lexeme;
	}
}

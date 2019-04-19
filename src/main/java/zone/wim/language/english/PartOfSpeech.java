package zone.wim.language.english;

import zone.wim.language.*;
import edu.mit.jwi.item.POS;
import zone.wim.item.*;

public enum PartOfSpeech {
	
	ADJECTIVE {
		public Class<? extends Lexeme> getLexemeType() {
			return Adjective.class;
		}
		public POS getWordnetPOS() {
			return POS.ADJECTIVE;
		}
	},
	VERB {
		public Class<? extends Lexeme> getLexemeType() {
			return Verb.class;
		}
		public POS getWordnetPOS() {
			return POS.VERB;
		}
	},
	ADVERB {
		public Class<? extends Lexeme> getLexemeType() {
			return Adverb.class;
		}
		public POS getWordnetPOS() {
			return POS.ADVERB;
		}
	},
	NOUN {
		public Class<? extends Lexeme> getLexemeType() {
			return Noun.class;
		}
		
		public POS getWordnetPOS() {
			return POS.NOUN;
		}
	};
	
	public abstract Class<? extends Lexeme> getLexemeType();
	public abstract POS getWordnetPOS();
	
	public static PartOfSpeech getByWordnetPOS(POS pos) {
		switch (pos) {
		case NOUN:
			return NOUN;
		case VERB:
			return VERB;
		case ADJECTIVE:
			return ADJECTIVE;
		case ADVERB:
			return ADVERB;
		}
		return null;
	}
}

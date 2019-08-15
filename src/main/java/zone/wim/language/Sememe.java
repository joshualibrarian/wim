package zone.wim.language;

import zone.wim.item.*;

import zone.wim.token.*;
import java.util.*;

import javafx.scene.layout.Pane;

public class Sememe extends BaseItem {
	
	String synsetId;
	Map<Language, String> gloss;
	Map<Language, Map<String, Lexeme>> lexemes;
	
	public Sememe(Address address, Signer signer) {
		super(address, signer);
		initialize();
	}
	
	private void initialize() {
		gloss = new HashMap<>();
		lexemes = new HashMap<>();
	}
	
	public void addLexeme(Language language, Lexeme lexeme) {
		Map<String, Lexeme> map = lexemes.get(language);
		if (map == null) {
			map = new HashMap<String, Lexeme>();
		}
		map.put(lexeme.getLemma(), lexeme);
		this.lexemes.put(language, map);
	}
	
	public Map<String, Lexeme> getLexemes(Language language) {
		return lexemes.get(language);
	}
	
	public Lexeme getLexemes(Language language, String lemma) {
		return lexemes.get(language).get(lemma);
	}
	
	public void setSynsetId(String referenceId) {
		this.synsetId = referenceId;
	}
	
	public String getSynsetId() {
		return synsetId;
	}
	
	public void setGloss(Language language, String gloss) {
		this.gloss.put(language, gloss);
	}
}

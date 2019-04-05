package zone.wim.language;

import java.util.*;

import zone.wim.token.*;

public abstract class Lexeme {
	
	private String lemma;
	private Locale locale;
	private Map<WordForm, String> forms;
	
	public Lexeme(String lemma) {
		this.lemma = lemma;
	}
	
	public String getLemma() {
		return lemma;
	}
	
	public Locale getLocale() {
		return locale;
	}
}

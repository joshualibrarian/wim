package zone.wim.language;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import zone.wim.exception.LibraryException.NotInitialized;
import zone.wim.item.*;
import zone.wim.token.*;
import zone.wim.library.*;

public class Fragment implements ChangeListener<String> {

	List<String> textHistory;
	String text;

	Reference subject;
	Reference object;
	
	ListProperty<Element> tokens;
	
	boolean valid;
	
	public Fragment() {
		initialize(); 
	} 
	
//	public Fragment(String text) {
//		this.text = text;
//		initialize();
//		parse();
//	}
	
	private void initialize() {
		ObservableList<Element> observableList =  FXCollections.observableArrayList();
		tokens = new SimpleListProperty<Element>(observableList);
	}
	
	public ListProperty<Element> tokensProperty() {
		return tokens;
	}
	
	public List<Element> getTokens() { 
		return tokens.getValue();
	}
	
	public String text() {
		return text;
	}
	
	public void setText(String text) {
		if (this.text != null) {
			textHistory.add(this.text);
		}
		this.text = text;
		evaluate();
	}
	
	public void evaluate() {
		String[] toks = tokenize(text);
		String[] previousToks = tokenize(textHistory.get(textHistory.size()));

		for(int x = 0; x < toks.length; x++) {
			if (toks[x] != previousToks[x]) {
				Element element = null;
				try {
					element = tokens.get(x);
				} catch(IndexOutOfBoundsException e) {
					element = new Element(toks[x]);
				}
				element.evaluate();
			}
		}
		
		
	}
	/**
	 * 
	 * this is encapsulated to allow easy overriding for non-whitespace languages
	 * 
	 * @param s
	 * @return
	 */
	public String[] tokenize(String s) {
		return s.split("[\\s]+");
	}
	
	@Override
	public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		setText(newValue);
		
	}
	
	public class Element {
		String text;
		Item value = null;
		ListProperty<Item> possibles;
	
		private Element(String t) {
			text = t;
			possibles = new SimpleListProperty<Item>(FXCollections.observableArrayList());
			
			if (!t.isEmpty()) {
				evaluate();
			}
		}
		
		
		private void evaluate() {
			try {
//				List<Token> possibleTypes = Token.parse(text);
				
				
			} catch (Throwable e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			List<Item> items;
			try {
				items = Library.instance().getItemsByWord(text);
				
				for(Item i : items) {
//					Reference r = new Reference(i);
//					r.asEntered(text);
					possibles.add(i);
				}
				if (possibles.size() == 1) {
					//if there is only one possible, we may as well set it
					value = possibles.get(0);
				} else if (possibles.size() < 1) {
					//do nothing here?
				} else {
					//here we have more than one possible, we may have to query the user
					//for now just try the first one
//					value = possibles.get(0);
				}
			} catch (NotInitialized e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		public String text() {
			return text;
		}
		
		public void text(String text) {
			this.text = text;
			evaluate();
		}
		
		public Item value() {
			return value;
		}
		
		public ListProperty<Item> possiblesProperty() {
			return possibles;
		}
		
		public List<Item> getPossibles() {
			return possibles.getValue();
		}
		
		
	}

}

package zone.wim.exception;

import java.util.List;
import zone.wim.token.Token;

@SuppressWarnings("serial")
public class TypeException extends Exception {
	
	public static class	Unknown extends Exception {
		public Unknown(String type) {
			super(type);
		}
	}
	
	public static class Invalid extends Exception {
		public Invalid(String type) {
			super(type);
		}

		public Invalid(Exception cause) {
			super(cause);
		}
	}
	
	public static class Duplicate extends Exception {
		List<Token> duplicates;
	
		public Duplicate(String type) {
			super(type);
		}
		
		public Duplicate(List<Token> duplicates) {
			this.duplicates = duplicates;
		}
	}
}

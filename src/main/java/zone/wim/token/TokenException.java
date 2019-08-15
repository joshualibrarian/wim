package zone.wim.token;

import java.util.List;

@SuppressWarnings("serial")
public class TokenException extends Exception {

	public static class Unknown extends Exception {
		public Unknown(String token) {
			super(token);
		}
	}
	
	public static class Invalid extends Exception {
		public Invalid(Throwable cause) {
			super(cause);
		}
	}
	
	public static class Ambiguous extends Exception {
		public Ambiguous(Throwable cause) {
			super(cause);
		}
		
		public Ambiguous(String text, List<Token> tokens) {
			super(text);
		}
	}
}


package zone.wim.token;

import zone.wim.coding.SelfCoding;

import java.util.List;

public class SelfCodingException {

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
		
		public Ambiguous(String text, List<SelfCoding> tokens) {
			super(text);
		}
	}
}


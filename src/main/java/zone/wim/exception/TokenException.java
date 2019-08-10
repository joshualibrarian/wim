package zone.wim.exception;

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
}

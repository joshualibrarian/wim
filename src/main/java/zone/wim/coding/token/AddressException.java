package zone.wim.coding.token;

@SuppressWarnings("serial")
public class AddressException extends Exception {
	AddressException(String message) {
		super(message);
	}
	public static class	Unknown extends AddressException {
		public Unknown(String address) {
			super(address);
		}
	}
	
	public static class Invalid extends Exception {
		public Invalid(String address) {
			super(address);
		}
		
		public Invalid(Throwable cause) {
			super(cause);
		}
	}
	
	public static class Duplicate extends Exception {
		public Duplicate(String address) {
			super(address);
		}
	}
}

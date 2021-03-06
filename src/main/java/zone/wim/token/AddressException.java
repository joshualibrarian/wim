package zone.wim.token;

@SuppressWarnings("serial")
public class AddressException {
	public static class	Unknown extends Exception {
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

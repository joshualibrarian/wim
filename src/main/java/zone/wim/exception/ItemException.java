package zone.wim.exception;

@SuppressWarnings("serial")
public class ItemException extends Exception {
	
	public static class	NotFound extends Exception {
		public NotFound(String address) {
			super(address);
		}
		
	}
	public static class SignersOnly extends Exception {
		public SignersOnly(String address) {
			super(address);
		}
	}

}

package zone.wim.exception;

public class StoreException extends Exception {
	public static class	NotFound extends Exception {
		public NotFound(String address) {
			super(address);
		}
		
	}
}

package zone.wim.library.store;

public class IndexException extends Exception {
	public static class	NotFound extends Exception {
		public NotFound(String address, Throwable cause) {
			super(address, cause);
		}
	}
	
	public static class	Duplicate extends Exception {
		public Duplicate(String address, Throwable cause) {
			super(address, cause);
		}
	}
}

package zone.wim.exception;

public class LibraryException {
	@SuppressWarnings("serial")
	public static class	NotInitialized extends Exception {
		public NotInitialized(Throwable cause) {
			super(cause);
		}
		
		public NotInitialized() {
			super();
		}
	}
	
	public static class InitFailure extends Exception {
		public InitFailure(Throwable cause) {
			super(cause);
		}
		
	}
}

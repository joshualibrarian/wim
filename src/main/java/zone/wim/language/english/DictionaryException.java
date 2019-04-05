package zone.wim.language.english;

public class DictionaryException {
	public static class	NotFound extends Exception {
		public NotFound(String url) {
			super(url);
		}
		
		public NotFound(Throwable cause) {
			super(cause);
		}

	}
}

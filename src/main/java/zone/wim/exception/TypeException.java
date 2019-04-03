package zone.wim.exception;

@SuppressWarnings("serial")
public class TypeException extends Exception {
	
	public static class	Unknown extends Exception {
		public Unknown(String type) {
			super(type);
		}
	}
	
	public static class Invalid extends Exception {
		public Invalid(String type) {
			super(type);
		}
	}
	
	public static class Duplicate extends Exception {
		public Duplicate(String type) {
			super(type);
		}
	}
}

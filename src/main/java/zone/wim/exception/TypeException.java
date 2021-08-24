package zone.wim.exception;

import zone.wim.coding.SelfCoding;

import java.util.List;

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

		public Invalid(Exception cause) {
			super(cause);
		}
	}
	
	public static class Duplicate extends Exception {
		List<SelfCoding> duplicates;
	
		public Duplicate(String type) {
			super(type);
		}
		
		public Duplicate(List<SelfCoding> duplicates) {
			this.duplicates = duplicates;
		}
	}
}

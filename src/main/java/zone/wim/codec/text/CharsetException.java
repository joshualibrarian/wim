package zone.wim.codec.text;

public class CharsetException {
	
	public static class NotFound {
		String charset;
		
		public NotFound(String charset) {
			this.charset = charset;
		}
	}
	
	public static class CodingEscaped {
		int escapeChar;
		
		public CodingEscaped(int escapeChar) {
			this.escapeChar = escapeChar;
		}
	}

}

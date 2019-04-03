package zone.wim.exception;

@SuppressWarnings("serial")
public class DataException extends Exception {
	String data;
	
	public DataException(Throwable cause) {
		super(cause);
	}
	
	public DataException(String data) {
		this.data = data;
	}
}

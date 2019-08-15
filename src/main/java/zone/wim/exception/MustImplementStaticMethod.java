package zone.wim.exception;

@SuppressWarnings("serial")
public class MustImplementStaticMethod extends Exception {
	
	public MustImplementStaticMethod(Exception cause) {
		super(cause);
	}
}

package zone.wim.codec;

import java.util.Iterator;

import zone.wim.codec.text.Charset;
import zone.wim.codec.text.UTF_8;

public class EscapeCharsetProvider {

	public Iterator<Charset> charsets() {
		// TODO Auto-generated method stub
		return null;
	}

	public static Charset charsetForName(String charsetName) {
		return new UTF_8();
	}

}

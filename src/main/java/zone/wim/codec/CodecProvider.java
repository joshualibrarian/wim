package zone.wim.codec;

import java.util.Iterator;

import zone.wim.codec.text.TextCodec;
import zone.wim.codec.text.unicode.UTF_8;

public class CodecProvider {

	public Iterator<TextCodec> charsets() {
		// TODO Auto-generated method stub
		return null;
	}

	public static TextCodec charsetForName(String charsetName) {
		return new UTF_8();
	}

}

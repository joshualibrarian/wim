package zone.wim.item.tokens;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import zone.wim.codec.DecodeAdapter;
import zone.wim.codec.EncodeAdapter;
import zone.wim.token.AddressException;
import zone.wim.token.Token;
import zone.wim.token.AddressException.Invalid;
import zone.wim.token.AtDomainAddress.Regex;

public abstract class ControlCharToken implements Token {

	public static class Regex {
		static String CHAR_CONTROLLED_TOKEN = "(?<pre>.)?(?<control>\\p{Cntrl})(?<post>.)?";
		static Pattern TOKEN = Pattern.compile(CHAR_CONTROLLED_TOKEN);
	}

	protected String text;
	protected String controlChar;
	protected String preText;
	protected String postText;
//	protected int controlCharPosition;
	
	public static ControlCharToken decode(DecodeAdapter adapter) {
		return null;
	}
	
	public ControlCharToken(DecodeAdapter adapter) throws Invalid {
		LOGGER.info("CharControlledToken(" + adapter.toString() + ")");
		
		for (int x = 0; x < text.length(); x++) {
//			if ()
		}
		
		
		Matcher m = Regex.TOKEN.matcher(text);
		
		if (!m.matches()) {
			throw new Invalid(text);
		}

		controlChar = m.group("control");
		preText = m.group("pre");
		postText = m.group("post");
		this.text = text;
	}
	
	@Override
	public String getText() {
		return text;
	}

	@Override
	public void setText(String text) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public void encode(EncodeAdapter adapter) {
		adapter.write(preText);
		adapter.write(controlChar);
		adapter.write(postText);
	}

}
package zone.wim.item.tokens;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Setter;
import zone.wim.coding.DecodeAdapter;
import zone.wim.coding.EncodeAdapter;
import zone.wim.coding.SelfCoding;
import zone.wim.coding.token.AddressException.Invalid;
import zone.wim.item.Item;

enum ConsecutiveTokens {
	INDEX(IndexToken, Item.INDEX_CHAR),
	VERSION(),
	TEMPORAL(),
	SECURITY(),
}

public abstract class ControlCharToken implements SelfCoding {

	public static class Regex {
		static String CHAR_CONTROLLED_TOKEN = "(?<pre>.)?(?<control>\\p{Cntrl})(?<post>.)?";
		static Pattern PATTERN = Pattern.compile(CHAR_CONTROLLED_TOKEN);
	}

	@Setter protected int controlChar;
	protected String text;
	protected String preText;
	protected String postText;
//	protected int controlCharPosition;
	
	public static ControlCharToken decode(DecodeAdapter adapter) {
		Stack<Object> preCoded = adapter.preCoded();
		while (!preCoded.isEmpty()) {
			Integer i = (Integer)preCoded.pop();

		}

	}
	
	public ControlCharToken(DecodeAdapter adapter) throws Invalid {

		for (int x = 0; x < text.length(); x++) {
//			if ()
		}
		
		
		Matcher m = Regex.PATTERN.matcher(text);
		
		if (!m.matches()) {
			throw new Invalid(text);
		}

		controlChar = Character.codePointAt(m.group("control"), 0);
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

	public abstract int controlChar();

}
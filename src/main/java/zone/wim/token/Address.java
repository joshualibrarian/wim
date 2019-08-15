package zone.wim.token;

import java.util.List;

import javax.jdo.annotations.EmbeddedOnly;

import zone.wim.item.Reference;
import zone.wim.item.Signer;
import zone.wim.token.AddressException.*;

@EmbeddedOnly
public interface Address extends Token {
	public static class Regex {
		public static String[] ENDINGS = {
			Reference.MANIFEST_CHAR,
			Reference.CONTENT_CHAR,
			Reference.RELATION_CHAR,
			"\\" + Reference.SUMMARY_CHAR 
		};
		public static String[] RESERVED = {
			"\\s",
//			"\\p{Cntrl}",
//			"\\[", "\\]",
//			"\\(", "\\)"
		};
		public static String ENDING_CHARS = String.join("", ENDINGS);
		public static String RESERVED_CHARS = String.join("", RESERVED);
		
	}
	
	public static Address parse(String address) throws Exception {
		return (Address) Token.parse(address, Address.class);
	}
	
	public Address generate(Signer creator, String name, ItemType type) throws Invalid;
	
	public String getSitePart();

	public default String getUserPart() {
		return null;
	}
	public default String getThingPart() {
		return null;
	}
	
	public boolean validate(String addressToValidate);
}

package zone.wim.token;

import java.util.List;

import javax.jdo.annotations.EmbeddedOnly;
import javax.persistence.Embeddable;

import zone.wim.exception.AddressException;
import zone.wim.exception.AddressException.*;
import zone.wim.item.Reference;

@Embeddable
@EmbeddedOnly
public interface Address extends Token {
	public static class Regex {
		public static String[] RESERVED = {
			Reference.MANIFEST_CHAR,
			Reference.CONTENT_CHAR,
			Reference.RELATION_CHAR,
			"\\" + Reference.SUMMARY_CHAR,
			"\\[", "\\]",
			"\\(", "\\)",
			"\\p{Cntrl}",
			"\\s"
		};
		public static String RESERVED_CHARS = String.join("", RESERVED);
	}
	
	public static Address parse(String address) throws Throwable {
		List<Token> results = Token.parse(address, Address.class);
		if (results.isEmpty()) {
			throw new Invalid(address);
		} else if (results.size() > 1) {
			throw new AddressException.Duplicate(address);
		}
		return (Address)results.get(0);
	}
	
	public String get();
	public String getUserPart();
	public String getPathPart();
	public boolean validate(String addressToValidate);
}

package zone.wim.coding.token;

import javax.jdo.annotations.EmbeddedOnly;

import zone.wim.coding.DecodeAdapter;
import zone.wim.coding.SelfCoding;
import zone.wim.item.Reference;
import zone.wim.item.Signer;
import zone.wim.coding.token.AddressException.*;
import zone.wim.token.ItemType;

@EmbeddedOnly
public interface Address extends SelfCoding {
	public static class Regex {
		public static String[] ENDINGS = {
			Reference.MANIFEST_CHAR,
			Reference.CONTENT_CHAR,
			Reference.RELATION_CHAR,
			"\\" + Reference.SUMMARY_CHAR 
		};
		public static String[] RESERVED = {
			"\\p{Space}",
			"\\p{Cntrl}",
			"\\"
		};
		public static String ENDING_CHARS = String.join("", ENDINGS);
		public static String RESERVED_CHARS = String.join("", RESERVED);
		
	}
	public static Address decode(DecodeAdapter adapter) throws Exception {


	}
	
	public static Address parse(String address) throws Exception {
		return (Address) SelfCoding.decode(address, Address.class);
	}
	
	public Address generate(Signer creator, String name, ItemType type) throws Invalid;
	
	public String sitePart();
	public default String uUserPart() {
		return null;
	}
	public default String thingPart() {
		return null;
	}
	
	public boolean validate(String addressToValidate);
}

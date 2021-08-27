package zone.wim.item.components;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import zone.wim.coding.DecodeAdapter;
import zone.wim.coding.SelfCoding;
import zone.wim.coding.SelfCodingException;
import zone.wim.coding.token.Address;
import zone.wim.item.*;
import zone.wim.item.tokens.Security;

@AllArgsConstructor
@Getter
enum ItemComponentType {
	MANIFEST (Manifest.class, Item.MANIFEST_CHAR),
	SUMMARY (Summary.class, Item.SUMMARY_CHAR),
	CONTENT (Content.class, Item.CONTENT_CHAR),
	RELATION (Relation.class, Item.RELATION_CHAR);

	Class<? extends ItemComponent> clazz;
	int specifyingChar;

	static int[] chars() {
		return Arrays.stream(values())
				.map(t -> t.specifyingChar())
				.mapToInt(c -> c.intValue()).toArray();
	}
	static ItemComponentType type(int codepoint) {
		for( ItemComponentType t : values()) {
			if (t.specifyingChar == codepoint) {
				return t;
			}
		}
		return null;
	}
}

public abstract class ItemComponent implements SelfCoding {

	public static ItemComponent decode(DecodeAdapter adapter) throws SelfCodingException.IncorrectlyCoded {
//		adapter.mark();

		Address a = (Address)adapter.expect(Address.class, ItemComponentType.chars());
		adapter.preCoded().push(a);

		ItemComponentType t = ItemComponentType.type(adapter.lastDecodedChar());
		return (ItemComponent)adapter.expect(t.clazz(), Item.TOKENIZER_CHAR);

	}
	
//	public static ItemComponent parse(UnicodeReader<ItemComponent> parser) {
//		ItemComponent result = null;
//		String currentChar = null;
//		do {
//			currentChar = parser.nextCharAsString();
//			
//		} while(result == null);
//		return result;
//	}
	
	protected Item enclosingItem = null;		// Reference ?
	protected Signer creator = null;
	protected String address;
	
	protected String index;
	protected String version;
	protected Date timestamp;
	protected Security security = Security.PRIVATE;
	protected List<Signature> signatures;
	
	protected ItemComponent(Item enclosingItem, Signer creator, Security security) {
		this.enclosingItem = enclosingItem;
		this.creator = creator;
		this.security = security;
		this.timestamp = new Date();
		
		this.index = enclosingItem.generateIndex(this);
	}
	
	protected ItemComponent(Reference enclosingItem, DecodeAdapter adapter) {

	}
	
	protected ItemComponent(String address) {
		this.address = address;
	}

}

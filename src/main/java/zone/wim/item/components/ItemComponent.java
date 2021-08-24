package zone.wim.item.components;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.PrimitiveIterator;
import javax.jdo.annotations.EmbeddedOnly;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import zone.wim.coding.DecodeAdapter;
import zone.wim.coding.EncodeAdapter;
import zone.wim.coding.SelfCoding;
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
}

public abstract class ItemComponent implements SelfCoding {

	public static ItemComponent decode(DecodeAdapter adapter) {

//		adapter.expect(Summary.class, Manifest.class, Content.class, Relation.class);
		adapter.mark();
		expect(Address.class, ItemComponentType.chars());


		ItemComponent result = null;
		
		adapter.currentToken();
		
		
		return result;
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

	protected void encodeTimestamp(EncodeAdapter adapter) {
		adapter.write(Item.TIMESTAMP_CHAR);
		adapter.write(timestamp);
	}
	
	protected void decodeTimestamp(DecodeAdapter adapter) {
		
	}
	
	protected void encodeSecurity(EncodeAdapter adapter) {
		adapter.write(Item.SECURITY_CHAR);
		adapter.write(security);
	}
	
	protected void encodeSignatures(EncodeAdapter adapter) {
		
	}
	
	public Item enclosingItem() {
		return enclosingItem;
	}
	
	public void enclosingItem(Item enclosingItem) {
		this.enclosingItem = enclosingItem;
	}

	public Date timestamp() {
		return timestamp;
	}
	
	public Security security() {
		return security;
	}
	
	public void security(Security security) {
		this.security = security;
	}
	
	public List<Signature> signatures() {
		return signatures;
	}
	
	public void signatures(List<Signature> signatures) {
		this.signatures = signatures;
	}
}

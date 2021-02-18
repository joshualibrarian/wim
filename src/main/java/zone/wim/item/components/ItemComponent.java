package zone.wim.item.components;

import java.io.InputStream;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Date;
import java.util.List;
import javax.jdo.annotations.EmbeddedOnly;

import zone.wim.codec.DecodeAdapter;
import zone.wim.codec.EncodeAdapter;
import zone.wim.codec.SelfCoding;
import zone.wim.item.*;
import zone.wim.item.tokens.Security;

@EmbeddedOnly
public abstract class ItemComponent implements SelfCoding {

	public static ItemComponent decode(DecodeAdapter adapter) {
		
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

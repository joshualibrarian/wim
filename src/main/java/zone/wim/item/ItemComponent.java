package zone.wim.item;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.List;
import javax.jdo.annotations.EmbeddedOnly;

import zone.wim.library.SelfParsing;
import zone.wim.library.EncodingAdapter;

@EmbeddedOnly
public abstract class ItemComponent implements SelfParsing {
	
	public static char INDEX_CHAR = '\u001C';			// FS
	public static char TIMESTAMP_CHAR = '\u001D';		// GS
	public static char SECURITY_CHAR = '\u001E';		// RS
	public static char SIGNATURE_CHAR = '\u001F';		// US
		
//	public static ItemComponent parse(UnicodeReader<ItemComponent> parser) {
//		ItemComponent result = null;
//		String currentChar = null;
//		do {
//			currentChar = parser.nextCharAsString();
//			
//		} while(result == null);
//		return result;
//	}
	
	protected Item enclosingItem;		// Reference ?
	protected String address;
	
	protected String index;
	protected Date timestamp;
	protected int security = 0;
	protected List<Signature> signatures;
	
	protected ItemComponent(Item enclosingItem, Signer creator, int security) {
		this.enclosingItem = enclosingItem;
		this.security = security;
		this.timestamp = new Date();
		
		this.index = enclosingItem.generateIndex(this);
	}
	
	protected ItemComponent(String address) {
		this.address = address;
	}
	
	public Item getEnclosingItem() {
		return enclosingItem;
	}
	
	public void setEnclosingItem(Item enclosingItem) {
		this.enclosingItem = enclosingItem;
	}

	public Date getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public int getSecurity() {
		return security;
	}
	
	public void setSecurity(int security) {
		this.security = security;
	}
	
	public List<Signature> getSignatures() {
		return signatures;
	}
	
	public void setSignatures(List<Signature> signatures) {
		this.signatures = signatures;
	}
	
	public abstract char referenceCharacter();
	
	public abstract ByteBuffer serialize(boolean relative);
}

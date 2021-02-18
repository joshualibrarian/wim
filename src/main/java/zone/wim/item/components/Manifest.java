package zone.wim.item.components;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Date;

import java.util.List;

import javax.jdo.annotations.EmbeddedOnly;

import zone.wim.codec.DecodeAdapter;
import zone.wim.codec.EncodeAdapter;
import zone.wim.item.Item;
import zone.wim.item.Signer;
import zone.wim.item.tokens.Security;
import zone.wim.token.Address;
import zone.wim.token.ComponentReference;

@EmbeddedOnly
public class Manifest extends ItemComponent {
	
	public static Manifest parse(DecodeAdapter adapter) {
		return null;
		
	}
	
	protected List<Address> addresses;
	protected List<ComponentReference> references;
	
	public Manifest(Item enclosingItem, Signer creator, Security security) {
		super(enclosingItem, creator, security);
	}
	
	public void encode(EncodeAdapter adapter) {
		CharBuffer buffer = CharBuffer.allocate(2048);
		
		// write addresses
		int addressCount = addresses.size() - 1;
		for (int x = 0; x < addressCount; x++) {
//			buffer.put(addresses.get(x).getText());
			adapter.write(addresses.get(x).getText());
			if (x < addressCount) {
//				buffer.put(Item.SPACE_CHAR);
				adapter.write(Item.SPACE_CHAR);
			}
		}
		
		buffer.put(Item.MANIFEST_CHAR);
		
		if (creator != null) {
			creator.address.getText();
		}
		
		buffer.put(Item.SPACE_CHAR);
		
		
		encodeTimestamp(adapter);		
		
		encodeSecurity(adapter);
		
		int referenceCount = references.size() -1;
		for (int x = 0; x < referenceCount; x++) {
			adapter.write(references.get(x).getText());
			adapter.write(Item.SPACE_CHAR);
		}
		
		
	}
}

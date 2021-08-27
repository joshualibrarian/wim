package zone.wim.item.components;

import java.nio.CharBuffer;

import java.util.List;

import javax.jdo.annotations.EmbeddedOnly;
import javax.persistence.criteria.CriteriaBuilder;

import zone.wim.coding.*;
import zone.wim.item.Item;
import zone.wim.item.Signer;
import zone.wim.item.tokens.IndexToken;
import zone.wim.item.tokens.Security;
import zone.wim.coding.token.Address;
import zone.wim.token.ComponentReference;
import zone.wim.coding.SelfCodingException.*;

@SelfCodingUnit(name = "test", returns = "test",  parameters = {"test"},  exceptions = { "test"})
public class Manifest extends ItemComponent implements SelfCoding {
	public static int SIGNIFYING_CHAR = Item.MANIFEST_CHAR;

	public static Manifest decode(DecodeAdapter adapter) throws IncorrectlyCoded {
		List<Object> preCoded = adapter.preCoded();
		Address a = null;
		if (preCoded.size() == 0) {
			a = (Address)adapter.expect(Manifest.class, Manifest.SIGNIFYING_CHAR);
		} else if (preCoded.size() == 1) {
			a = (Address) preCoded.get(1);
		} else {
			throw new IncorrectlyCoded(Address.class, a);
		}
		if(adapter.expectCodepoint() != SIGNIFYING_CHAR)
			throw new IncorrectlyCoded("Manifest missing signifying char");
		if(adapter.expectCodepoint() != Item.TOKENIZER_CHAR)
			throw new IncorrectlyCoded(adapter.lastDecodedChar(), Item.TOKENIZER_CHAR);
		IndexToken index = (IndexToken)adapter.expect(IndexToken.class);


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
				adapter.write(Item.TOKENIZER_CHAR);
			}
		}
		
		buffer.put(Item.MANIFEST_CHAR);
		
		if (creator != null) {
			creator.address.getText();
		}
		
		buffer.put(Item.TOKENIZER_CHAR);

		
		int referenceCount = references.size() -1;
		for (int x = 0; x < referenceCount; x++) {
			adapter.write(references.get(x).getText());
			adapter.write(Item.TOKENIZER_CHAR);
		}
		
		
	}
}

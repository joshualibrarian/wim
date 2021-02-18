package zone.wim.item.components;

import java.security.KeyPair;

import java.security.PublicKey;
import java.util.List;
import javax.jdo.annotations.EmbeddedOnly;

import zone.wim.codec.EncodeAdapter;
import zone.wim.item.Item;
import zone.wim.item.Reference;
import zone.wim.item.Signer;
import zone.wim.item.tokens.Security;
import zone.wim.token.*;

@EmbeddedOnly
public class Content extends ItemComponent {

	String name;
	String blurb;
	long size = -1;
	List<DataLocation> data;
	
	protected Content(Reference enclosingItem, Signer creator, Security security) {
		super(enclosingItem.item(), creator, security);
	}
	
	protected Content(Item enclosingItem, Signer creator) {		
		super(enclosingItem, creator, 0xFF);
		
	}
	
	protected Content(Item enclosingItem, PrivateKey privateKey) {
		
	}
	protected Content(PublicKey publicKey) {
		
	}

	@Override
	public void encode(EncodeAdapter adapter) {
//		adapter.write(enclosingItem.address);
		enclosingItem.getAddress().encode(adapter);
		adapter.write(name);
		encodeIndex(adapter);
		encodeVersion()
		
	}
	
	public char encodeAddressElement() {
		return Item.CONTENT_CHAR;
	}
}

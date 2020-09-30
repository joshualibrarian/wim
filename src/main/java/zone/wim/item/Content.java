package zone.wim.item;

import java.security.KeyPair;

import java.security.PublicKey;
import java.util.List;
import javax.jdo.annotations.EmbeddedOnly;

import zone.wim.library.EncodeAdapter;
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
		name = "public.key";
		
		super(enclosingItem, creator, 0xFF);
		
	}
	
	protected Content(Item enclosingItem, PrivateKey privateKey) {
		
	}
	protected Content(PublicKey publicKey) {
		
	}

	@Override
	public void encode(EncodeAdapter adapter) {
		// TODO Auto-generated method stub
		
	}
}

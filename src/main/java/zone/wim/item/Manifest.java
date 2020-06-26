package zone.wim.item;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Date;

import java.util.List;

import javax.jdo.annotations.EmbeddedOnly;

import zone.wim.library.DecodeAdapter;
import zone.wim.library.EncodeAdapter;
import zone.wim.token.Address;
import zone.wim.token.ComponentReference;

@EmbeddedOnly
public class Manifest extends ItemComponent {
	
	protected List<Address> addresses;
	protected List<ComponentReference> references;
	
	public Manifest(Item enclosingItem, Signer creator, Security security) {
		super(enclosingItem, creator, security);
	}
	
	public ByteBuffer write() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.address);
		sb.append(Item.MANIFEST_CHAR);
		
		sb.append(Item.SPACE_CHAR);
		
		if (index > 0 ) {
			sb.append(Integer.toString(index));
		}
		
		sb.append(Integer.toString(security));
	}

	@Override
	public void read(ByteBuffer bytes) {
		// TODO Auto-generated method stub
		
	}
}

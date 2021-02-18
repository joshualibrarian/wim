package zone.wim.item.components;

import java.util.List;
import javax.jdo.annotations.*;

import zone.wim.codec.DecodeAdapter;
import zone.wim.codec.EncodeAdapter;
import zone.wim.item.Reference;
import zone.wim.item.Signer;
import zone.wim.item.tokens.Security;

@EmbeddedOnly
public class Relation extends ItemComponent {

	
	private Signer creator;
	private int index;
	private Reference subject;
	private List<Reference> linking;
	private Reference object;
	
	protected Relation(Reference enclosingItem, Signer creator, Security security) {
		super(enclosingItem.item(), creator, security);
	}

	protected Relation(DecodeAdapter adapter) {
		super(adapter);
	}
	
	@Override
	public void encode(EncodeAdapter adapter) {
		// TODO Auto-generated method stub
		
	}
}

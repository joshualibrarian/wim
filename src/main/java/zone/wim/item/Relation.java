package zone.wim.item;

import java.util.List;
import javax.jdo.annotations.*;

import zone.wim.library.EncodeAdapter;

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

	@Override
	public void encode(EncodeAdapter adapter) {
		// TODO Auto-generated method stub
		
	}
}

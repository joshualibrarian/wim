package zone.wim.item.components;

import java.util.List;
import javax.jdo.annotations.*;

import zone.wim.coding.DecodeAdapter;
import zone.wim.coding.EncodeAdapter;
import zone.wim.coding.SelfCoding;
import zone.wim.item.Reference;
import zone.wim.item.Signer;
import zone.wim.item.tokens.Security;
import zone.wim.coding.token.Address;

@EmbeddedOnly
public class Relation extends ItemComponent implements SelfCoding {

	public static Relation decode(DecodeAdapter adapter) {
		Relation result = null;

		adapter.expect(Address.class);


		return result;

	}

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
	
//	protected void decode(DecodeAdapter adapter) {
//		
//	}
}

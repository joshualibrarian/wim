package zone.wim.socket.protocol;

import zone.wim.coding.DecodeAdapter;
import zone.wim.coding.EncodeAdapter;
import zone.wim.item.Item;
import zone.wim.item.Signer;
import zone.wim.item.components.ItemComponent;

public class Delivery extends ProtocolComponent {
	public static char IDENTIFYING_CHAR = 'd';

	public static Delivery decode(DecodeAdapter adapter) {

		adapter.expect(ItemComponent.class, Item.TOKENIZER_CHAR);
	}


	//	protected boolean encrypted = false;
	
	public Delivery(Signer from, Signer to, ItemComponent[] payload) {
		super(from, to, payload);
	}

	
	@Override
	public void encode(EncodeAdapter adapter) {
		adapter.write(ProtocolComponent.PROTOCOL_CHAR);
		adapter.write(IDENTIFYING_CHAR);
		for (ItemComponent component : payload) {
			adapter.write(Item.TOKENIZER_CHAR);
			component.encode(adapter);
		}
		
		
		
	}

}

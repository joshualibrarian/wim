package zone.wim.protocol;

import zone.wim.codec.EncodeAdapter;
import zone.wim.item.Item;
import zone.wim.item.Signer;
import zone.wim.item.components.ItemComponent;

public class Delivery extends ProtocolComponent {

	//	protected boolean encrypted = false;
	
	public Delivery(Signer from, Signer to, ItemComponent[] payload) {
		super(from, to, payload);
	}

	
	@Override
	public void encode(EncodeAdapter adapter) {
		adapter.write(ProtocolComponent.PROTOCOL_CHAR);
		adapter.write(ProtocolComponent.DELIVERY_CHAR);
		for (ItemComponent component : payload) {
			adapter.write(Item.SPACE_CHAR);
			component.encode(adapter);
		}
		
		
		
	}

}

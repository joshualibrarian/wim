package zone.wim.socket.protocol;

import zone.wim.coding.EncodeAdapter;
import zone.wim.item.Signer;
import zone.wim.item.components.ItemComponent;

public class Request extends ProtocolComponent {

	public static char IDENTIFYING_CHAR = 'r';

	public Request(Signer from, Signer to, ItemComponent[] payload) {
		super(from, to, payload);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void encode(EncodeAdapter adapter) {

		
	}

}

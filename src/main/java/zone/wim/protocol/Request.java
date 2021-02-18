package zone.wim.protocol;

import zone.wim.codec.DecodeAdapter;
import zone.wim.codec.EncodeAdapter;
import zone.wim.codec.SelfCoding;
import zone.wim.item.Signer;
import zone.wim.item.components.ItemComponent;

public class Request extends ProtocolComponent {
	
	public Request(Signer from, Signer to, ItemComponent[] payload) {
		super(from, to, payload);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void encode(EncodeAdapter adapter) {
		adapter.write(ProtocolComponent.PROTOCOL_CHAR);
		adapter.write(ProtocolComponent.ßREQUEST_CHAR);
		
	}

}

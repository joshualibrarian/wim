package zone.wim.socket.protocol;

import zone.wim.coding.CodingException.*;
import zone.wim.coding.DecodeAdapter;
import zone.wim.coding.SelfCoding;
import zone.wim.item.Signer;
import zone.wim.item.components.ItemComponent;

public abstract class ProtocolComponent implements SelfCoding {
	
	public static char PROTOCOL_CHAR = '\u0010';

	public static char TO_CHAR = 't';
	public static char FROM_CHAR = 'f';

	public static ProtocolComponent decode(DecodeAdapter adapter) throws InvalidData {
		ProtocolComponent result = null;
		int firstCodepoint = adapter.expectCodepoint();
		if (firstCodepoint != PROTOCOL_CHAR) {
			throw new InvalidData("protocol component must begin with " + PROTOCOL_CHAR);
		}
		int secondCodepoint = adapter.expectCodepoint();
		if (secondCodepoint == Request.IDENTIFYING_CHAR) {
			result = Request.decode(adapter);
		} else if (secondCodepoint == Delivery.IDENTIFYING_CHAR) {
			result = Delivery.decode(adapter);
		} else {
			throw new InvalidData("command character of '" + secondCodepoint + "' not recognized");
		}
		return result;
	}
	
	protected Signer from;
	protected Signer to;
	protected ItemComponent[] payload;

	public ProtocolComponent(Signer from, Signer to, ItemComponent[] payload) {
		
		
	}	

}

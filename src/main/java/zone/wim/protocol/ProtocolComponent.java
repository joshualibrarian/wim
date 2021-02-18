package zone.wim.protocol;

import zone.wim.codec.DecodeAdapter;
import zone.wim.codec.SelfCoding;
import zone.wim.item.Signer;
import zone.wim.item.components.ItemComponent;

public abstract class ProtocolComponent implements SelfCoding {
	
	public static char PROTOCOL_CHAR = '\u0010';
	public static char REQUEST_CHAR = 'r';
	public static char DELIVERY_CHAR = 'd';

	public static char TO_CHAR = 't';
	public static char FROM_CHAR = 'f';

	public static ProtocolComponent parse(DecodeAdapter adapter) {
		CharSequence token = adapter.currentToken();
		return null;
	}
	
	protected Signer from;
	protected Signer to;
	protected ItemComponent[] payload;

	public ProtocolComponent(Signer from, Signer to, ItemComponent[] payload) {
		
		
	}	

}

package zone.wim.protocol;

import zone.wim.library.DecodeAdapter;
import zone.wim.library.SelfCoding;

public abstract class ProtocolComponent implements SelfCoding {
	
	public static ProtocolComponent parse(DecodeAdapter adapter) {
		return null;
	}

}

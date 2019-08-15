package zone.wim.item;

import java.util.Map;
import javax.jdo.annotations.EmbeddedOnly;
import zone.wim.token.Type;

@EmbeddedOnly
public class Summary extends ItemComponent {
	
	private Type rootContentType;
	private Map<Reference, String> pairs;
	
	protected Summary(Reference enclosingItem, int security) {
		super(enclosingItem, security);
	}
}

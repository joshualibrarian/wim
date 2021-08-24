package zone.wim.item.components;

import java.util.Map;
import javax.jdo.annotations.EmbeddedOnly;

import zone.wim.coding.token.Type;

@EmbeddedOnly
public class Summary extends ItemComponent {
	
	private Type rootContentType;
	private Map<Reference, String> pairs;
	
	protected Summary(Reference enclosingItem, int security) {
		super(enclosingItem, security);
	}
	
	@Override
	public String referenceCharacter() {
		return "\u0012";	// DC2
	}
}

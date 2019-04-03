package zone.wim.item;

import java.util.Map;

import javax.jdo.annotations.EmbeddedOnly;
import javax.persistence.Embeddable;

import zone.wim.token.Type;

@Embeddable
@EmbeddedOnly
public class Summary extends ItemComponent {
	
	private Type rootContentType;
	private Map<Reference, String> pairs;
}

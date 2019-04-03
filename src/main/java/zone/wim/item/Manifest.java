package zone.wim.item;

import java.util.Date;
import java.util.List;

import javax.jdo.annotations.EmbeddedOnly;
import javax.persistence.Embeddable;

import zone.wim.token.ComponentReference;

@Embeddable
@EmbeddedOnly
public class Manifest extends ItemComponent {

	int index;
	private List<ComponentReference> references;
	
}

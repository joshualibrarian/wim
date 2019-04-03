package zone.wim.item;

import java.util.Date;

import java.util.List;

import javax.jdo.annotations.EmbeddedOnly;
import javax.persistence.Embeddable;

@Embeddable
@EmbeddedOnly
public abstract class ItemComponent {
	
	private Reference enclosingItem;
	private Date timestamp;
	private int security;
	
	private List<Signature> signatures;

}

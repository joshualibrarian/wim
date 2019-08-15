package zone.wim.item;

import java.util.Date;

import java.util.List;

import javax.jdo.annotations.EmbeddedOnly;

import zone.wim.token.ComponentReference;

@EmbeddedOnly
public class Manifest extends ItemComponent {

	int index;
	private List<ComponentReference> references;
	
	public Manifest(Reference enclosingItem, int security, List<ComponentReference> references) {
		super(enclosingItem, security);
		this.references = references;
	}
	
}

package zone.wim.item;

import java.util.List;
import javax.jdo.annotations.*;

@EmbeddedOnly
public class Relation extends ItemComponent {

	private Signer creator;
	private int index;
	private Reference subject;
	private List<Reference> linking;
	private Reference object;
}

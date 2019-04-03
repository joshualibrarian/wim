package zone.wim.item;

import java.util.List;

import javax.jdo.annotations.EmbeddedOnly;
import javax.persistence.Embeddable;
import zone.wim.token.*;

@Embeddable
@EmbeddedOnly
public class Content extends ItemComponent {

	String name;
	String blurb;
	long size = -1;
	List<DataLocation> data;
}

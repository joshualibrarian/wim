package zone.wim.item;

import java.util.List;


import javax.jdo.annotations.EmbeddedOnly;
import zone.wim.token.*;

@EmbeddedOnly
public class Content extends ItemComponent {

	String name;
	String blurb;
	long size = -1;
	List<DataLocation> data;
}

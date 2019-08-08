package zone.wim.item;

import java.util.List;

import javafx.beans.property.ListProperty;
import zone.wim.client.ItemControl;

public interface Group extends Item {

	public ListProperty<Item> contentsProperty();
	public List<Item> getContents();
}

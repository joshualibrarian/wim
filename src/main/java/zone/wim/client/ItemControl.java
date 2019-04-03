package zone.wim.client;

import javafx.scene.control.Control;
import zone.wim.item.Item;

public class ItemControl extends Control {

	Item item;
	public ItemControl(Item item) {
		this.item = item;
	}
}

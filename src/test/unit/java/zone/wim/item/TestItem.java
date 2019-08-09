package zone.wim.item;

import javax.persistence.Entity;

import javafx.scene.layout.Pane;
import zone.wim.token.Address;

@Entity
public class TestItem extends AbstractItem {
	public TestItem(String address) {
		super();
		try {
			setAddress(Address.parse(address));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

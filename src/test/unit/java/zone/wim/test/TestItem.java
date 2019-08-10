package zone.wim.test;

import javax.jdo.annotations.PersistenceCapable;

import javafx.scene.layout.Pane;
import zone.wim.token.Address;

@PersistenceCapable
public class TestItem extends BaseItem {
	public TestItem(String address) {
		super();

		setAddressKey(address);

		try {
			setAddress(Address.parse(address));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}

package zone.wim.item;

import javax.jdo.annotations.PersistenceCapable;

import zone.wim.item.BaseItem;
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

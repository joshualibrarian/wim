package zone.wim.item;

import javax.jdo.annotations.PersistenceCapable;

import zone.wim.coding.token.Address;

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

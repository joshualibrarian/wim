package zone.wim.item;

import org.junit.jupiter.api.BeforeAll;
import zone.wim.coding.token.Address;

public class ItemTest {

	@BeforeAll
	public void createSimpleItem() {
		try {
			Address address = Address.parse("@foo.net");
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

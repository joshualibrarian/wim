package zone.wim.item;

import java.util.logging.Logger;
import javax.jdo.annotations.EmbeddedOnly;
import zone.wim.exception.LibraryException.NotInitialized;
import zone.wim.item.components.Content;
import zone.wim.item.components.Relation;
import zone.wim.library.Library;
import zone.wim.coding.token.Address;

@EmbeddedOnly
public class Reference {
	private static Logger LOGGER = Logger.getLogger(Reference.class.getCanonicalName());
	
	public static final String MANIFEST_CHAR = "~";
	public static final String RELATION_CHAR = ";";
	public static final String CONTENT_CHAR = "^";
	public static final String SUMMARY_CHAR = "`";
	
	transient private Item item = null;
	
	private Address address = null;
	private String literal = null;			// STX ... ETX
	private String asEntered = null;		//  (  ...  )
	private Relation relation = null;		//  [  ...  ]
	private Content range = null;			// 	[  ...  ]	
	
	public Reference(Item item) {
		this.item = item;
		address = item.getAddress();
	}
	
	public Reference(Address address) { 
		this.address = address;
	}
	
	public Reference(String referenceText) throws Throwable {
		address = Address.parse(referenceText);
	}
	
	public Item item() { 
		if (item == null) {
			try {
				item = Library.instance().getItemByAddress(address.getText());
			} catch (NotInitialized e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return item; 
	}
	
	@Override
	public String toString() {
		String s = address.getText();
		
		if (literal != null) {
			s += "(" + literal + ")";
		}
		//TODO: etc.
		
		return s;
	}
	
	@Override
	public boolean equals(Object o){
		  if(o instanceof Reference){
		    Reference r = (Reference) o;
		    return this.address.equals(r.address);
		  }
		  return false;
	}
	
	@Override
	public int hashCode() {
	    return address.hashCode();
	}
}

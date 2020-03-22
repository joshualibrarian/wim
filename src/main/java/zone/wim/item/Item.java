package zone.wim.item;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.*;
import javax.jdo.annotations.*;

import org.apache.commons.collections4.MultiValuedMap;

import zone.wim.client.*;
import zone.wim.library.SelfParsing;
import zone.wim.token.*;

@PersistenceCapable
public interface Item {
	
	public static char SPACE_CHAR = '\u0020';
	
	// common across items
	public static char INDEX_CHAR = '\u001C';		// FS
	public static char SECURITY_CHAR = '\u001D';	// GS
	public static char TIMESTAMP_CHAR = '\u001E';	// RS
	public static char SIGNATURE_CHAR = '\u001F';	// US
	public static char TYPE_CHAR = '\u001A';		// SUB
	
	// manifest
	public static char MANIFEST_CHAR = '\u0011';	// DC1
	
	
	// relation chars
	public static char RELATION_CHAR = '\u0012';	// DC2
	public static char OBJECT_CHAR = '\u0001';		// SOH
	
	// content chars
	public static char CONTENT_CHAR = '\u0013';		// DC3
	public static char DATA_CHAR = '\u0007';		// BEL
	
	// summary chars
	public static char SUMMARY_CHAR = '\u0014';		// DC4
	public static char COUNT_CHAR = '\u0009';		// HT
	
	@PrimaryKey
	public String getAddressKey();
	public void setAddressKey(String key);
	
	public Address getAddress();
	public void setAddress(Address address);
	
	public MultiValuedMap<Reference, String> getWords();

	public List<String> getWords(Reference reference);
	
	public List<Manifest> getManifests(Signer requestor);
	public List<Summary> getSummaries(Signer requestor);
	public List<Content> getContents(Signer requestor);
	public List<Relation> getRelations(Signer requstor);
	
	public List<Relation> relationsRelatedBy(Item... items);
	public List<Relation> relationsRelatedTo(Item...items);
	public List<Relation> relationsCreatedBy(Signer creator);
	
	public ItemUserInterface getUserInterface();
	
	public String generateIndex(ItemComponent component);
	
	public default boolean isGroup() {
		return (this instanceof Group);
	}
	
	public ByteBuffer serialize();
	
//	public List<Class<? extends Item>> getCanCreate();
//	public List<Class<? extends Item>> getCanNotCreate();
	
//	public abstract Address generateAddress(String name, ItemType type) throws Invalid;

}

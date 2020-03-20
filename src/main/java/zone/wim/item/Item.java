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
public interface Item extends SelfParsing {
	
	public static String MANIFEST_CHAR = "\u0011";
	public static String RELATION_CHAR = "\u0012";
	public static String CONTENT_CHAR = "\u0013";
	public static String SUMMARY_CHAR = "\u0014";
	
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
	
	public byte[] serialize();
	
//	public List<Class<? extends Item>> getCanCreate();
//	public List<Class<? extends Item>> getCanNotCreate();
	
//	public abstract Address generateAddress(String name, ItemType type) throws Invalid;

}

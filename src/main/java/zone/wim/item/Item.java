package zone.wim.item;

import java.io.InputStream;

import java.nio.ByteBuffer;
import java.util.*;
import javax.jdo.annotations.*;

import org.apache.commons.collections4.MultiValuedMap;

import zone.wim.client.*;
import zone.wim.codec.Codec;
import zone.wim.codec.DecodeAdapter;
import zone.wim.codec.SelfCoding;
import zone.wim.codec.text.Charset;
import zone.wim.item.components.Content;
import zone.wim.item.components.ItemComponent;
import zone.wim.item.components.Manifest;
import zone.wim.item.components.Relation;
import zone.wim.item.components.Summary;
import zone.wim.token.*;

@PersistenceCapable
public interface Item extends SelfCoding {
	
	public static char SPACE_CHAR = '\u0020';
	
	// component identifying chars
	public static char MANIFEST_CHAR = '\u0011';	// DC1
	public static char RELATION_CHAR = '\u0012';	// DC2
	public static char CONTENT_CHAR = '\u0013';		// DC3
	public static char SUMMARY_CHAR = '\u0014';		// DC4
		
	// common across items
	public static char INDEX_CHAR = '\u001C';		// FS
	public static char SECURITY_CHAR = '\u001D';	// GS
	public static char TIMESTAMP_CHAR = '\u001E';	// RS
	public static char SIGNATURE_CHAR = '\u001F';	// US
	
	public static char ENCRYPTION_CHAR = '\u0005';	// ENQ
	
	public static char TYPE_CHAR = '\u001A';		// SUB
	public static char DELETE_CHAR = '\u007F';		// DEL
	public static char VERSION_CHAR = '\u0016';		// SYN
	
	public static char COUNT_CHAR = '\u0009';		// HT
	public static char BINARY_CHAR = '\u0007';		// BEL

	// relation specific
	public static char OBJECT_CHAR = '\u0001';		// SOH
	
	public static Item parse(DecodeAdapter decodeAdapter) {
		List<ItemComponent> components = new ArrayList<>();
		
		while(true) {
			
		}
	}
	
	@PrimaryKey
	public String getAddressKey();
	public void setAddressKey(String key);
	
	public Address getAddress();
	public void setAddress(Address address);
	
	public MultiValuedMap<Reference, String> words();

	public List<String> words(Reference reference);
	
	public List<Manifest> manifests(Signer requestor);
	public List<Summary> summaries(Signer requestor);
	public List<Content> contents(Signer requestor);
	public List<Relation> relations(Signer requstor);
	
	public List<Relation> relationsRelatedBy(Signer requestor, Item... items);
	public List<Relation> relationsRelatedTo(Signer requestor, Item... items);
	public List<Relation> relationsCreatedBy(Signer requestor, Signer creator);
	
	public ItemUserInterface getUserInterface();
	
	public String generateIndex(ItemComponent component);
	
	public default boolean isGroup() {
		return (this instanceof Group);
	}
	
	public default Charset preferredEncoding() {
		return Charset.UTF_8;
	}
	
//	public abstract Address generateAddress(String name, ItemType type) throws Invalid;
}
 
package zone.wim.item;

import java.util.*;
import javax.jdo.annotations.*;

import org.apache.commons.collections4.MultiValuedMap;

import zone.wim.ui.*;
import zone.wim.coding.DecodeAdapter;
import zone.wim.coding.SelfCoding;
import zone.wim.coding.text.TextCodec;
import zone.wim.coding.text.unicode.UnicodeCodec;
import zone.wim.item.components.Content;
import zone.wim.item.components.ItemComponent;
import zone.wim.item.components.Manifest;
import zone.wim.item.components.Relation;
import zone.wim.item.components.Summary;
import zone.wim.coding.token.Address;

@PersistenceCapable
public interface Item extends SelfCoding {
	public static char TOKENIZER_CHAR = '\u0020';
	public static char NEWLINE_CHAR = '\n';

	public static int SHIFT_OUT = '\u000E';
	public static int SHIFT_IN = '\u000F';

	// component identifying chars
	public static char MANIFEST_CHAR = '\u0011';	// DC1
	public static char RELATION_CHAR = '\u0012';	// DC2
	public static char CONTENT_CHAR = '\u0013';		// DC3
	public static char SUMMARY_CHAR = '\u0014';		// DC4
		
	// common across items
	public static char INDEX_CHAR = '\u001C';		// FS
	public static char SECURITY_CHAR = '\u001D';	// GS
	public static char TIMESTAMP_CHAR = '\u001E';	// RS
	public static char VERSION_CHAR = '\u001F';		// US
	
	public static char ENCRYPTION_CHAR = '\u0005';	// ENQ
	
	public static char TYPE_CHAR = '\u001A';		// SUB
	public static char DELETE_CHAR = '\u007F';		// DEL
	public static char SIGNATURE_CHAR = '\u0016';	// SYN
	
	public static char COUNT_CHAR = '\u0009';		// HT
	public static char BINARY_CHAR = '\u0007';		// BEL

	// relation specific
	public static char OBJECT_CHAR = '\u0001';		// SOH

	public static Item decode(DecodeAdapter adapter) {
		Item item = null;

		List<Summary> summaries = new ArrayList<>();
		List<Manifest> manifests = new ArrayList<>();
		List<Content> contents = new ArrayList<>();
		List<Relation> relations = new ArrayList<>();

		// loop until we are no longer finding item components
		while(true) {
			ItemComponent c = (ItemComponent)adapter.expect(ItemComponent.class, Item.NEWLINE_CHAR);
			if (c instanceof Summary) {
				summaries.add((Summary)c);
			} else if (c instanceof Manifest) {
				manifests.add((Manifest)c);
			} else if (c instanceof Content) {
				contents.add((Content)c);
			} else if (c instanceof Relation) {
				relations.add((Relation)c);
			} else {
				break;
			}
		}

		Address address = null;
		List<ItemComponent> l = null;
		if (!manifests.isEmpty()) {
			for (Manifest m : manifests) {
//				addressx
			}
		} else if (summaries.size() > 0) {

		}
		if (summaries.size() > 0 || manifests.size() > 0) {
			//TODO: here's where I construct the item from the item components

		}
		return item;
	}

	@PrimaryKey
	public String getAddressKey();
	public void setAddressKey(String key);
	
	public Address getAddress();
	public void setAddress(Address address);
	
	public MultiValuedMap<Reference, String> words();

	public List<String> words(Reference reference);
	
	public List<Manifest> manifests(Signer requester);
	public List<Summary> summaries(Signer requester);
	public List<Content> contents(Signer requester);
	public List<Relation> relations(Signer requester);
	
	public List<Relation> relationsRelatedBy(Signer requester, Item... items);
	public List<Relation> relationsRelatedTo(Signer requester, Item... items);
	public List<Relation> relationsCreatedBy(Signer requester, Signer creator);
	
	public ItemUserInterface getUserInterface();
	
	public String generateIndex(ItemComponent component);
	
	public default boolean isGroup() {
		return (this instanceof Group);
	}
	
	public default TextCodec preferredEncoding() {
		return UnicodeCodec.UTF_8;
	}
	
//	public abstract Address generateAddress(String name, ItemType type) throws Invalid;
}
 
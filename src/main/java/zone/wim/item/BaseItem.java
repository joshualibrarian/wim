package zone.wim.item;

import java.util.*;


import java.util.logging.Logger;

import javax.jdo.annotations.*;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

import zone.wim.client.*;
import zone.wim.token.*;
import zone.wim.token.AddressException.Invalid;
import zone.wim.exception.ItemException.*;

@PersistenceCapable
public abstract class BaseItem implements Item {
	private static Logger LOGGER = Logger.getLogger(BaseItem.class.getName());

	private String addressKey;
	
	transient protected Address address;
	transient protected ItemUserInterface userInterface;

	protected List<Manifest> manifests;
	protected List<Summary> summaries;
	protected List<Relation> relations;
	protected List<Content> contents;

	protected MultiValuedMap<Reference, String> words;
	
	protected transient ItemControl control;
	protected transient Reference creator;
	protected transient int security = 0;

	protected BaseItem() {
		LOGGER.info("AbstractItem()");
		initialize();
	}
	
	protected BaseItem(Address address) throws SignersOnly {
		if (!(this instanceof Signer)) {
			throw new SignersOnly(address.getText());
		}
		setAddress(address);
		initialize();
	}
	
	protected BaseItem(Address address, Signer creator) {
		setAddress(address);
		if (creator != null) {
			this.creator = new Reference(creator);
		}
		initialize();
	}
	
	private void initialize() {
		security = 0;

		manifests = new ArrayList<Manifest>();
		relations = new ArrayList<Relation>();
		contents = new ArrayList<Content>();
		words = new ArrayListValuedHashMap<Reference, String>();
	}

	@PrimaryKey
	public String getAddressKey() {
		return addressKey;
	}

	public void setAddressKey(String key) {
		this.addressKey = key;
	}
	
	public Address getAddress() {
		return address;
	}
	
	public void setAddress(Address address) {
		this.address = address;
		this.addressKey = address.getText();
	}
	
	public MultiValuedMap<Reference, String> getWords() {
		return words;
	}
	
	public List<String> getWords(Reference reference) {
		return (List<String>)words.get(reference);
	}
	
	public ItemControl getControl() {
		if (!(control instanceof ItemControl)) {
			control = new ItemControl(this);
		}
		
		return control;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Item) {
			Item other = (Item)obj;
			return addressKey == other.getAddressKey();
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
	    return addressKey.hashCode();
	}
	
//	@Override
	public List<Relation> getRelations() {
		return relations;
	}
	
	@Override
	public List<Relation> getRelations(Relation... relations) {
		
		// TODO
		return null;
	}
	
	@Override
	public ItemUserInterface getUserInterface() {
		if (!(userInterface instanceof ItemUserInterface)) {
			userInterface = new ItemUserInterface(this);
		}
		return userInterface;
	}
}

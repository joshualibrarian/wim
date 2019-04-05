package zone.wim.item;

import java.util.*;
import java.util.logging.Logger;

import javax.jdo.annotations.*;
import javax.persistence.*;
import zone.wim.client.*;
import zone.wim.token.*;
import zone.wim.exception.ItemException.*;

@PersistenceCapable
@MappedSuperclass
public class Item {
	private static Logger LOGGER = Logger.getLogger(Item.class.getName());

	@PrimaryKey
	@Id private String addressKey;
	protected Address address;

	protected boolean draft = false;

	protected List<Manifest> manifests;
	protected List<Summary> summaries;
	protected List<Relation> relations;
	protected List<Content> contents;

	@ManyToMany
	protected List<Token> tokens;
	
	protected transient ItemControl control;
	protected transient Reference creator;
	protected transient int security = 0;

	public Item() {
		LOGGER.info("Item()");
		initialize();
	}
	
	public Item(Address address) throws SignersOnly {
		if (!(this instanceof Signer)) {
			throw new SignersOnly(address.get());
		}
		setAddress(address);
		initialize();
	}
	
	public Item(Address address, Signer creator) {
		setAddress(address);
		if (creator != null) {
			this.creator = new Reference(creator);
		}
		initialize();
	}
	
	private void initialize() {
		draft = true;
		security = 0;

		manifests = new ArrayList<Manifest>();
		relations = new ArrayList<Relation>();
		contents = new ArrayList<Content>();
		tokens = new ArrayList<Token>();
	}

	protected void setAddress(Address address) {
		this.address = address;
		this.addressKey = address.get();
	}
	
	public List<Token> getTokens() {
		return this.getTokens(Token.class);
	}

	public List<Token> getTokens(Class<? extends Token> tokenType) {
		List<Token> tokens = new ArrayList<>();
		// TODO: process the relations for only very reasonable tokens to return
		return tokens;
	}
	
	public ItemControl getControl() {
		if (!(control instanceof ItemControl)) {
			control = new ItemControl(this);
		}
		
		return control;
	}
	
	public Address getAddress() {
		return address;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Item) {
			Item other = (Item)obj;
			return addressKey == other.addressKey;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
	    return addressKey.hashCode();
	}
}

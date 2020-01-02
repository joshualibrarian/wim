package zone.wim.item;

import java.util.*;


import java.util.logging.Logger;

import javax.jdo.annotations.*;

import org.apache.commons.collections4.MultiValuedMap;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import zone.wim.client.*;
import zone.wim.token.*;
import zone.wim.token.AddressException.Invalid;
import zone.wim.exception.ItemException.*;

@PersistenceCapable
public abstract interface Item {
	
	@PrimaryKey
	public String getAddressKey();
	public void setAddressKey(String key);
	
	public Address getAddress();
	public void setAddress(Address address);
	
	public MultiValuedMap<Reference, String> getWords();

	public List<String> getWords(Reference reference);
	
	public List<Relation> getRelations();
	
	public List<Relation> getRelations(Relation... relations);
	
	public ItemUserInterface getUserInterface();
	
	public default boolean isGroup() {
		return (this instanceof Group);
	}
	
//	public List<Class<? extends Item>> getCanCreate();
//	public List<Class<? extends Item>> getCanNotCreate();
	
//	public abstract Address generateAddress(String name, ItemType type) throws Invalid;

}

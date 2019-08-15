package zone.wim.item;

import java.util.*;


import java.util.logging.Logger;

import javax.jdo.annotations.*;

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
	
	public List<Token> getTokens();

	public List<Token> getTokens(Class<? extends Token> tokenType);
	
//	public List<Relation> getRelations(); 
	
	public ItemUserInterface getUserInterface();
	
	public default boolean isGroup() {
		return (this instanceof Group);
	}
	
//	public abstract Address generateAddress(String name, ItemType type) throws Invalid;

}

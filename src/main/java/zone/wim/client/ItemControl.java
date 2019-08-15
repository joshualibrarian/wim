package zone.wim.client;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
//import zone.wim.item.Group;
import zone.wim.item.Item;

public class ItemControl extends Control {
	
	private Item item;
	private StringProperty address;
	private StringProperty title;
	
	public ItemControl(Item item) {
		this.item = item;
		getStyleClass().add("item-control");
		setFocusTraversable(true);
		
		address = new SimpleStringProperty(item.getAddress().getText());
	}
	
	public Item item() {
		return item;
	}
	
	public StringProperty addressProperty() { 
		if (address == null) {
			address = new SimpleStringProperty(this, "address");
		}
		
		return address; 
	}
	
	public String getAddress() {
		return address.get();
	}
	
	public StringProperty titleProperty() { 
		if (address == null) {
			address = new SimpleStringProperty(this, "address");
		}
		
		return address; 
	}
	
	public String getTitle() {
		if (address == null) {
			return null;
		}
		
		return address.get();
	}
	
	public void setAddress(String address) {
		this.address.set(address);
	}
	
	@Override
	protected Skin<?> createDefaultSkin() {
		return new ItemSkin(this);
	}
	
	
}

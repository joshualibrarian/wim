package zone.wim.item;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import zone.wim.client.QueryPane;
import zone.wim.language.Fragment;
import zone.wim.token.*;

public class Query extends BaseItem implements Group {
private static String WORD = "query";
	
	private String name = null;
	private ArrayList<String> raw;		//multiple versions for history
	private Fragment fragment;			//this stores and parses the sentance (some day multiple fragments!)
	private ListProperty<Item> results;
	
	/*
	public ListProperty<Item> getResults() {
		return results;
	}
	
	public ListProperty<Item> resultsProperty() {
		return results;
	}
	*/

//	public Query() {
//		initialize();
//	}
	
	public Query(Signer creator, Address address) throws AddressException.Invalid {
		super(address, creator);
		initialize();
	}
	
	private void initialize() {
		raw = new ArrayList<String>();
		fragment = new Fragment();
		ObservableList<Item> observableList =  FXCollections.observableArrayList();
		results = new SimpleListProperty<Item>(observableList);
	}
	
	public Item[] submit(Signer user) {
		List<Reference> refs = new ArrayList<>();
		
//		Item[] results = Library.instance().query(user, fragment.getValue().references());
//		return results;
		return new Item[0];
	}

	public Fragment getFragment() {
		return fragment;
	}
	
	@Override
	public ListProperty<Item> contentsProperty() {
		return results;
	}

	@Override
	public List<Item> getContents() {
		// TODO Auto-generated method stub
		return null;
	}
}

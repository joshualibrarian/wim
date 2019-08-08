package zone.wim.client;

import java.util.List;
import java.util.logging.Logger;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Sphere;
import zone.wim.item.*;

public class QueryPane extends VBox {
	
	private static Logger LOGGER = Logger.getLogger(QueryPane.class.getCanonicalName());
	TextField inputField;
	Button mapButton;
	Button timelineButton;
	HBox topBar;
	GroupPane resultsPane;
	
	FragmentPane fragmentPane;
	
	/**
	 * this constructor is used to create a public query 
	 */
	public QueryPane() {
		super();
	}
	
	public QueryPane(Query query) {
		super();
		setMinSize(600, 400);
		
		fragmentPane = new FragmentPane(query.getFragment());
		getChildren().add(fragmentPane);
		
		/*
		inputField = new TextField();
		inputField.setMinWidth(400);
		inputField.textProperty().addListener(query);
		
		mapButton = new Button(Client.Symbols.GLOBE);
		timelineButton = new Button(Client.Symbols.MANTLEPIECE_CLOCK);
		HBox.setHgrow(inputField, Priority.ALWAYS);
		topBar = new HBox(inputField, mapButton, timelineButton);
		
		getChildren().add(topBar);
		
		ClarifyPane clarifyPane = new ClarifyPane();
		getChildren().add(clarifyPane);
		*/
	}
	
	/*
	protected class ClarifyPane extends TilePane implements ListChangeListener<Fragment.Token> {

		private void setToken(int index, List<Item> possibles) {
			ListView<ItemControl> lv = new ListView<>();
			for (Item item : possibles) {
				lv.getItems().add(item.getControl());
			}
			getChildren().add(index, lv);
		}
		
		@Override
		public void onChanged(Change<? extends Fragment.Token> c) {
			
			
		}
		
	}
	*/
}

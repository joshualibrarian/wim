package zone.wim.ui;

import javafx.collections.ObservableList;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import zone.wim.item.*;

public class ItemListView  extends ListView<Item> {

	public ItemListView(ObservableList<Item> items) {
		super(items);
		setCellFactory(new ItemCellFactory());
	}
	
	public class ItemCellFactory implements Callback<ListView<Item>, ListCell<Item>> {

		@Override
		public ListCell<Item> call(ListView<Item> param) {
			return new ItemListCell();
		}
	}
	
	public class ItemListCell extends ListCell<Item> {

	     @Override 
	     protected void updateItem(Item item, boolean empty) {
	         // calling super here is very important - don't skip this!
	         super.updateItem(item, empty);
	           
	         if (empty || item == null) {
	        	 setText(null);
	        	 setGraphic(null);
	        	 
	         } else {
	        	 setText(item.getAddress().getText());	//todo: maybe remove this
		         setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		         ItemControl control = item.getUserInterface().getControl();
		         control.prefWidthProperty().bind(ItemListView.this.widthProperty().subtract(2));
		         setGraphic(control);
	         }
	         
//	         this.prefWidthProperty().bind(ItemListView.this.widthProperty().subtract(2));
	         
	     }
	 }
}

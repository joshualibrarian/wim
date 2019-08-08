package zone.wim.client;

import java.util.logging.Logger;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import zone.wim.item.*;
import zone.wim.language.Fragment;
import zone.wim.language.Fragment.Element;

public class FragmentPane extends VBox implements ListChangeListener<Fragment.Element>{
	
	Logger logger = Logger.getLogger(FragmentPane.class.getCanonicalName());
	
	Fragment fragment;
	TextArea textControl;
	FlowPane clarifyPane;
	
	public FragmentPane(Fragment fragment) {
		this.fragment = fragment;
		
		textControl = new TextArea(fragment.text());
		textControl.textProperty().addListener(fragment);
		HBox.setHgrow(textControl, Priority.ALWAYS);

		clarifyPane = new FlowPane();
		fragment.tokensProperty().addListener(this);
		
		getChildren().addAll(textControl, clarifyPane);
		
	}
	
	private void setToken(int index, ObservableList<Item> possibles) {
		ItemListView lv = new ItemListView(possibles);
		lv.setPrefWidth(250);
		clarifyPane.getChildren().add(index, lv);
	}
	
	
	@Override
	public void onChanged(Change<? extends Element> c) {
		while (c.next()) {
			
			if (c.wasPermutated()) {
//				LOGGER.debug("from {} to {} is permutated", c.getFrom(), c.getTo());
            	//loop through each token that has been mutated
                for (int t = c.getFrom(); t < c.getTo(); t++) {
                	setToken(t, c.getList().get(t).possiblesProperty());
                	
                }
            } else if (c.wasUpdated()) {
//            	LOGGER.debug("from {} to is updated", c.getFrom(), c.getTo());
            	//loop through each token that has been updated
                for (int t = c.getFrom(); t < c.getTo(); t++) {
                	setToken(t, c.getList().get(t).possiblesProperty());
                }
            } else {
//            	logger.debug("from {} to {} is add/removed", c.getFrom(), c.getTo());
                for (Fragment.Element removedItemToken : c.getRemoved()) {
//                	getChildren().remove(re)
//                	logger.debug("{} was removed", removedItemToken);
                }
                
                if (c.wasRemoved()) {
               	 	for (int t = c.getFrom(); t < c.getTo(); t++) {
//               		logger.debug("removing {}", t);
               	 		clarifyPane.getChildren().remove(t);
    				}
               }
                
               if (c.wasAdded()) {
                    for (int t = c.getFrom(); t < c.getTo(); t++) {
                    	setToken(t, c.getList().get(t).possiblesProperty());
    				}
               }
                
               
            }
			
		}	
	}
	/*
	protected class TokenPane extends FlowPane {
		private ListView<Item> listView;
		private Fragment.Token token;
		
		public TokenPane(Fragment.Token token) {
			this.token = token;
			listView = new ListView<Item>();
			for (Item item : token.getPossibles()) {
				listView.getItems().add(item);
				if (token.value() != null) {
					if (token.value().equals(item)) {
						//TODO replace this with a pseudo class
						this.getStyleClass().add("value");
					}
				}
			}
			
		}
	}
	*/
	
}

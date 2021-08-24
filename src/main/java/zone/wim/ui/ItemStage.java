package zone.wim.ui;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import zone.wim.item.*;

public class ItemStage extends Stage {

	Item item;
	BorderPane rootPane;
	
	ControlPane controlPane;
	Pane contentPane = null;
	GroupPane groupPane = null;
	
	public ItemStage(Item item) {
		super();
		this.item = item;
		rootPane = new BorderPane();
		
		Pane controlPane = new ControlPane(item.getUserInterface().getControl());
		rootPane.setTop(controlPane);
//		rootPane.getChildren().add(controlPane);

		contentPane = item.getUserInterface().getPane();
		
		if (item.isGroup()) {
			groupPane = new GroupPane((Group)item);
			rootPane.setCenter(groupPane);
			if (contentPane != null) {
				// TODO: control placement of secondary panel (auxiliary panels?)
				rootPane.setRight(contentPane);
			}
		} else if (contentPane != null) {
			rootPane.setCenter(contentPane);
		}
		
		this.setScene(new Scene(rootPane));
	}
	
	protected class ControlPane extends Pane {
		ItemControl control;
		
		ControlPane(ItemControl control) {
			super();
			this.control = control;
			ControlPane.this.getChildren().add(control);
		}
		
	}
	
}

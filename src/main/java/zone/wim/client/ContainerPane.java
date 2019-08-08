package zone.wim.client;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import zone.wim.item.*;

public class ContainerPane extends VBox {
	Group container;
	ViewOptions viewOptions;
	
	
	public ContainerPane(Group container) {
		this.container = container;
	}
	
	class ViewOptions extends HBox {
		ComboBox viewStyle;
		ComboBox showTypes;
		Button query;
	}
}

package zone.wim.client;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import zone.wim.item.Host;

public class HostPane extends VBox {
	Host host;
	
	public HostPane(Host host) {
		super();
		this.host = host;
		
		Label welcomeLabel = new Label("welcome to " + host.getAddressKey());
		getChildren().add(welcomeLabel);
		
		TextField siteName = new TextField();
		siteName.setOnAction(this::createSiteAction);
		Button createSiteButton = new Button("Create Site");
		getChildren().add(createSiteButton);
		
		
		Button createQueryButton = new Button("Create Query");
		getChildren().add(createQueryButton);
	}
	
	private void createSiteAction(ActionEvent event) {
//		host.createSite
	}
}

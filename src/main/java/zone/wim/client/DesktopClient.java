package zone.wim.client;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import zone.wim.item.Host;
import zone.wim.library.Library;

public class DesktopClient extends Application {
	
	private ItemStage localhostStage;
	private Library library;
	private Host localhost;
	
	@Override 
	public void init() {
		
	}
	
	@Override
	public void start(Stage defaultStage) throws Exception {
		library = Library.instance();
		localhost = library.getLocalhost();
		
		localhostStage = new ItemStage(localhost);
		localhostStage.show();
	}
	
	@Override
	public void stop() {
		
	}

}

package zone.wim.ui.graphical;

import javafx.application.Application;
import javafx.stage.Stage;
import zone.wim.item.Host;
import zone.wim.library.Library;
import zone.wim.ui.ItemStage;

public class GraphicalUI extends Application {
	
	private ItemStage localhostStage;
	private Library library;
	private Host localhost;
	
	@Override 
	public void init() {
		
	}
	
	@Override
	public void start(Stage defaultStage) throws Exception {
		library = Library.local();
		localhost = library.getLocalhost();
		
		localhostStage = new ItemStage(localhost);
		localhostStage.show();
	}
	
	@Override
	public void stop() {
		
	}

}

package zone.wim.client;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import zone.wim.client.ItemTreeView.ItemTreeItem;
import zone.wim.item.*;

public class GroupPane extends VBox implements ListChangeListener<Item> {

	VBox contentsPane;
	ItemTreeView treeView;
//	ListView<Item> listPane;
//	TableView<Item> tablePane;
	
	Group rootItem;
	ItemControl rootItemControl;

	Stage queryStage;
	Scene queryScene;
//	QueryPane queryPane;
	
	HBox toolbar;

	public GroupPane(Group rootItem) {
		// TODO add sanity checking here to be sure its really a Group
		this.rootItem = (Group) rootItem;
		Button sortButton = new Button("\u2295");
		Button buttonB = new Button("\u21c5");
		toolbar = new HBox(sortButton, buttonB);
		getChildren().add(toolbar);
		
		buildTreeView();
		
		rootItemControl = rootItem.getUserInterface().getControl();
		getChildren().add(rootItemControl);
		
		treeView = new ItemTreeView(rootItem.getUserInterface().getControl());
		treeView.setShowRoot(false);
		getChildren().add(treeView);
		
	}
	
	private void buildTreeView() {
		
	}
	
	/**
	 * called when the contents of this group changes
	 */
	@Override
	public void onChanged(Change<? extends Item> c) {
		System.out.println("GroupPane.onChanged(Change)");
	}
	
	/*
	private void openQueryPane(Query query) {
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

		queryPane = new QueryPane(query);
		queryStage = new Stage();
		queryStage.setScene(new Scene(queryPane));
		queryStage.initStyle(StageStyle.UTILITY);
		queryStage.setY(0);
		System.out.println("QUERY STAGE WIDTH: " + queryStage.getWidth());
		queryStage.setX((primaryScreenBounds.getWidth() / 2) - (queryStage.getMinWidth() / 2));
		queryStage.show();
	}
	*/
	
	
}

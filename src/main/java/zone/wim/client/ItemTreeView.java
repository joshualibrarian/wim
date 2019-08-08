package zone.wim.client;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import zone.wim.item.*;

public class ItemTreeView extends TreeView<ItemControl> {
	
	Item rootItem;

	public ItemTreeView(ItemControl control) {
		
	}

	protected class ItemTreeItem extends TreeItem<ItemControl> {
		
		private boolean isLeaf;
		
		private boolean isFirstTimeChildren = true;
		private boolean isFirstTimeLeaf = true;
		
		protected ItemTreeItem(ItemControl value) {
			super(value);
			
		}
		
		@Override
		public boolean isLeaf() {
			if (isFirstTimeLeaf) {
				isFirstTimeLeaf = false;
				
				Item item = getValue().item();
				isLeaf = item instanceof Group;
			}
			
			return isLeaf;
		}
		
		@Override
		public ObservableList<TreeItem<ItemControl>> getChildren() {
			if (isFirstTimeChildren) {
				isFirstTimeChildren = false;
				
				Item item = getValue().item();
				if (item instanceof Group) {
					for (Item i : ((Group)item).contentsProperty()) {
						getChildren().add(new ItemTreeItem(new ItemControl(i)));
						
					}
				}
			}
			
			return super.getChildren();
		}
		
	}

}

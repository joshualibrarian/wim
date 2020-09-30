package zone.wim.client;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.Sphere;
import zone.wim.item.Item;

public class ItemUserInterface {
	
	Item item;
	Color color = Color.BLUE;
	Node icon = null;
	Shape3D shape = null;
	Pane pane = null;
	Map<DeviceType, Pane> panes;
	
	public ItemUserInterface(Item item) {
		this.item = item;
		panes = new HashMap<>();
	}

	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public ItemControl getControl() {
		return new ItemControl(item);
	}
	
	public Pane getPane(DeviceType deviceType) {
		Pane pane = panes.get(deviceType);
		
		return pane;
		
	}
	
	public void setPane(Pane pane) {
		this.pane = pane;
	}
	
	public Node getIcon() {
		if (!(shape instanceof Node)) {
			shape = new Sphere(20);
			shape.setOpacity(.2);
			PhongMaterial material = new PhongMaterial();
			material.setDiffuseColor(color);
			shape.setMaterial(material);
		}
		return shape;
	}
	
	public Node getShape() {
		return null;
	}
}

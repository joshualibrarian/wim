package zone.wim.ui;

import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.control.SkinBase;
import javafx.scene.control.ToolBar;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Text;

public class ItemSkin extends SkinBase<ItemControl> {
	
	Text addressText;
	Sphere ball;
	Group group;
	
	public ItemSkin(ItemControl control) {
		super(control);

		ball = new Sphere(20);
		ball.setOpacity(.2);
		PhongMaterial material = new PhongMaterial();
		material.setDiffuseColor(Color.rgb(0, 0, 255, .4));
		ball.setMaterial(material);
		
		addressText = new Text();
		addressText.textProperty().bind(control.addressProperty());
		addressText.setTextOrigin(VPos.TOP);
		
		getChildren().add(ball);
		getChildren().add(addressText);
	} 
	
	@Override
	protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
		ball.setLayoutX(ball.getRadius());
		ball.setLayoutY(ball.getRadius());
		addressText.setX(0);
		addressText.setY(0);
	}

	@Override
	protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset,
			double leftInset) {
		// TODO Auto-generated method stub
		return super.computeMinWidth(height, topInset, rightInset, bottomInset, leftInset);
	}

	@Override
	protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset,
			double leftInset) {
		// TODO Auto-generated method stub
		return super.computeMinHeight(width, topInset, rightInset, bottomInset, leftInset);
	}

	@Override
	protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset,
			double leftInset) {
		return super.computeMaxWidth(height, topInset, rightInset, bottomInset, leftInset);
//		return 120;
	}

	@Override
	protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset,
			double leftInset) {
		return super.computeMaxHeight(width, topInset, rightInset, bottomInset, leftInset);
	}

	@Override
	protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset,
			double leftInset) {
		return super.computePrefWidth(height, topInset, rightInset, bottomInset, leftInset);
	}

	@Override
	protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset,
			double leftInset) {
		return super.computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);
	}

	
	protected class AuthenticationToolbar extends ToolBar {
		
		
		protected AuthenticationToolbar() {
			super();
		}
	}
	
	
}

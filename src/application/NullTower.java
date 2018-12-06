package application;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class NullTower extends Tower {
	
	private static Image[] idle;
	private static Image[] hover;
	
	static {
		// load image;
	}
	
	private static double width;
	private static double height;
	
	public NullTower(Field field, Point2D position, int direction) {
		super(field, position, direction);
	}

	@Override
	public void tick(long now, GraphicsContext gc) {
		draw(gc);
	}
	
	protected void draw(GraphicsContext gc) {
		double drawX = position.getX() - width / 2;
		double drawY = position.getY() - height;
		gc.drawImage(idle[direction], drawX, drawY);
	}
	
	public void hover() {
		
	}
	
	public void click() {
		
	}

}

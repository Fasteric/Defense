package application;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public abstract class Tower {
	
	protected Field field;
	protected Point2D position;
	protected int direction;
	
	public Tower(Field field, Point2D position, int direction) {
		this.field = field;
		this.position = position;
		this.direction = direction;
	}
	
	public abstract void tick(long now, GraphicsContext gc);
	
	public abstract void hover();
	
	public abstract void click();

}

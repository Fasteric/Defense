package application;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public class Lightning {
	
	Field field;
	Point2D position;
	
	private int lifeTime = 120;
	
	public Lightning(Field field, Point2D position) {
		this.field = field;
		this.position = position;
	}
	
	public void tick(long now, GraphicsContext gc) {
		
	}

}

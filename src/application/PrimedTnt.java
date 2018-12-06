package application;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class PrimedTnt {
	
	private static Image lit;
	private static Image unlit;
	
	static {
		// load image;
	}
	
	private static double width;
	private static double height;
	
	private Field field;
	
	private Point2D position;
	private Point2D destination;
	private Point2D horizontalMomentum;
	
	private int tickRemaining = 90;
	
	private boolean isLit;
	private boolean isExploded;
	
	
	public PrimedTnt(Field field, Point2D position, Point2D destination) {
		this.field = field;
		this.position = position;
		this.destination = destination;
		this.horizontalMomentum = PointOperations.different(position, destination);
		this.horizontalMomentum = PointOperations.scale(this.horizontalMomentum, 1/90d);
		this.isExploded = false;
	}
	
	
	public void tick(long now, GraphicsContext gc) {
		if (isExploded) return;
		
		tickRemaining--;
		if (tickRemaining < 0) {
			explode();
			return;
		}
		
		position = PointOperations.add(position, horizontalMomentum);
		isLit = tickRemaining % 30 >= 15;
		draw(gc);
	}
	
	private void draw(GraphicsContext gc) {
		double drawX = position.getX() - width / 2;
		double drawY = position.getY() - height - verticalTrajectory(tickRemaining);
		if (isLit) {
			gc.drawImage(lit, drawX, drawY);
		}
		else {
			gc.drawImage(unlit, drawX, drawY);
		}
	}
	
	private double verticalTrajectory(int tickRemaining) {
		double x = (tickRemaining - 45) / 45d;
		return 45 * (1 - (x * x));
	}
	
	private void explode() {
		if (isExploded) return; // make sure it only explode once
		// doDamage
		field.removeProjectile(this);
		isExploded = true;
	}

}

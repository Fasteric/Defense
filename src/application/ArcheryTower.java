package application;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class ArcheryTower extends Tower {
	
	private static Image[] idle;
	private static Image[] hover;
	
	private static Image[][] firingAnimation;
	private static int firingAnimationLength = 3;
	
	static {
		// load image
	}
	
	private static double width;
	private static double height;
	
	private static int prefiringDelay = 15;
	private static int postfiringDelay = 15;

	private static double radiusY = 150;
	private static double radiusX = 1.732 * radiusY;
	

	public ArcheryTower(Field field, Point2D position, int direction) {
		super(field, position, direction, prefiringDelay, postfiringDelay, radiusX, radiusY);
	}

	
	@Override
	protected void fire() {
		//Arrow arrow = new Arrow();
	}
	
	@Override
	protected void graphicUpdate(GraphicsContext gc) {
		
		double drawX = position.getX() - width / 2;
		double drawY = position.getY() - height;
		
		if (isSearching) {
			gc.drawImage(idle[direction], drawX, drawY);
		}
		
		if (lifeCycle >= -15 && lifeCycle < -10) {
			gc.drawImage(firingAnimation[direction][0], drawX, drawY);
		}
		if (lifeCycle >= -10 && lifeCycle < -5) {
			gc.drawImage(firingAnimation[direction][1], drawX, drawY);
		}
		if (lifeCycle >= -5 && lifeCycle < 0) {
			gc.drawImage(firingAnimation[direction][2], drawX, drawY);
		}
		if (lifeCycle >= 0 && lifeCycle < 15) {
			gc.drawImage(firingAnimation[direction][0], drawX, drawY);
		}
		
	}


	@Override
	public boolean hover(Point2D hoverPosition) {
		return false;
	}

	@Override
	public boolean click(Point2D pressPosition, Point2D releasePosition) {
		return false;
	}

	@Override
	public boolean unclick() {
		return false;
	}


	@Override
	public double getRenderPriority() {
		return position.getY();
	}

	@Override
	public int compareTo(Renderable other) {
		return Double.compare(getRenderPriority(), other.getRenderPriority());
	}
	
}

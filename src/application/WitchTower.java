package application;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class WitchTower extends Tower {
	
	private static Image[] idle;
	private static Image[] hover;
	
	private static Image[][] firingAnimation;
	private static int firingAnimationLength = 0;
	
	static {
		// load image
	}
	
	private static double width;
	private static double height;
	
	private static int prefiringDelay = 60;
	private static int postfiringDelay = 60;
	
	private static double radiusY = 70;
	private static double radiusX = 1.732 * radiusY;
	

	public WitchTower(Field field, Point2D position, int direction) {
		super(field, position, direction, prefiringDelay, postfiringDelay, radiusX, radiusY);
	}

	
	@Override
	protected void fire() {
		//ThrowPotion throwPotion = new ThrowPotion();
	}
	
	@Override
	protected void graphicUpdate(GraphicsContext gc) {
		
		double drawX = position.getX() - width / 2;
		double drawY = position.getY() - height;
		gc.drawImage(idle[direction], drawX, drawY);
		
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
	public void unclick() {
		
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

package application;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class NullTower extends Tower {
	
	private static Image[] idle;
	
	private static Image constructOption;
	
	static {
		// load image;
	}
	
	private static double width;
	private static double height;
	
	private static int prefiringDelay = 2147483647;
	private static int postfiringDelay = 2147483647;
	
	private static double radiusX = 0;
	private static double radiusY = 0;
	
	
	public NullTower(Field field, Point2D position, int direction) {
		super(field, position, direction, prefiringDelay, postfiringDelay, radiusX, radiusY);
	}

	
	@Override
	protected void graphicUpdate(GraphicsContext gc) {
		double drawX = position.getX() - width / 2;
		double drawY = position.getY() - height;
		gc.drawImage(idle[direction], drawX, drawY);
	}

	@Override
	protected void fire() {
		
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

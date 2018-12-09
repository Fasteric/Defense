package application;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class WitchTower extends Tower {
	
	private static Image[] idle = new Image[4];
	
	private static double width = 70;
	private static double height = 90;
	
	static {
		String format = "res/tower/witch%d.png";
		for (int i = 0; i < 4; i++) {
			idle[i] = new Image(String.format(format, i), width, height, true, false);
		}
	}
	
	private static int prefiringDelay = 60;
	private static int postfiringDelay = 60;

	private static double radius = 100;
	private static double radiusX = Math.sqrt(1.5) * radius;
	private static double radiusY = Math.sqrt(0.5) * radius;
	
	private static double projectileShift = 75;
	

	public WitchTower(Field field, Point2D position, int direction) {
		super(field, position, direction, prefiringDelay, postfiringDelay, radiusX, radiusY);
	}

	
	@Override
	protected void fire() {
		Point2D projectileInitial = new Point2D(position.getX(), position.getY() - projectileShift);
		ThrownPotion throwPotion = new ThrownPotion(field, projectileInitial, projectileDestination);
		field.addProjectile(throwPotion);
	}
	
	@Override
	protected void graphicUpdate(GraphicsContext gc) {
		
		double drawX = position.getX() - width / 2;
		double drawY = position.getY();
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

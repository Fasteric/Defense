package application;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class ArcheryTower extends Tower {
	
	private static Image[] idle = new Image[4];
	private static Image[][] firing = new Image[4][3];
	
	private static double width = 70;
	private static double height = 90;
	
	static {
		String format = "res/tower/stray%d%d.png";
		for (int i = 0; i < 4; i++) {
			idle[i] = new Image(String.format(format, i, 3), width, height, true, false);
			for (int j = 0; j < 3; j++) {
				firing[i][j] = new Image(String.format(format, i, j), width, height, true, false);
			}
		}
	}
	
	private static int prefiringDelay = 15;
	private static int postfiringDelay = 30;

	private static double radius = 200;
	private static double radiusX = Math.sqrt(1.5) * radius;
	private static double radiusY = Math.sqrt(0.5) * radius;
	
	private static double projectileShift = 30;
	

	public ArcheryTower(Field field, Point2D position, int direction) {
		super(field, position, direction, prefiringDelay, postfiringDelay, radiusX, radiusY);
	}

	
	@Override
	protected void fire() {
		Point2D projectileInitial = new Point2D(position.getX(), position.getY() - projectileShift);
		Arrow arrow = new Arrow(field, projectileInitial, projectileDestination);
		field.addProjectile(arrow);
	}
	
	@Override
	protected void graphicUpdate(GraphicsContext gc) {
		
		double drawX = position.getX() - width / 2;
		double drawY = position.getY() - height / 2;
		
		if (isSearching) {
			gc.drawImage(idle[direction], drawX, drawY);
			return;
		}
		
		if (isPrefiring) {
			if (lifeCycle >= -15 && lifeCycle < -10) {
				gc.drawImage(firing[direction][0], drawX, drawY);
			}
			if (lifeCycle >= -10 && lifeCycle < -5) {
				gc.drawImage(firing[direction][1], drawX, drawY);
			}
			if (lifeCycle >= -5 && lifeCycle < 0) {
				gc.drawImage(firing[direction][2], drawX, drawY);
			}
			return;
		}
		
		if (isPostfiring) {
			gc.drawImage(firing[direction][0], drawX, drawY);
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

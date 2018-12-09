package application;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class ArtilleryTower extends Tower {
	
	private static Image[] idle = new Image[4];
	private static Image[] firing = new Image[4];
	
	private static double width = 70;
	private static double height = 90;
	
	static {
		String format = "res/tower/tnt%d%d.png";
		for (int i = 0; i < 4; i++) {
			idle[i] = new Image(String.format(format, i, 0), width, height, true, false);
			firing[i] = new Image(String.format(format, i, 1), width, height, true, false);
		}
	}
	
	private static int prefiringDelay = 120;
	private static int postfiringDelay = 120;

	private static double radius = 150;
	private static double radiusX = Math.sqrt(1.5) * radius;
	private static double radiusY = Math.sqrt(0.5) * radius;
	
	private static double projectileShift = 75;
	

	public ArtilleryTower(Field field, Point2D position, int direction) {
		super(field, position, direction, prefiringDelay, postfiringDelay, radiusX, radiusY);
	}

	
	@Override
	protected void fire() {
		System.out.println("DEBUG : Artillery.fire from to : " + position + " -> " + projectileDestination);
		Point2D projectileInitial = new Point2D(position.getX(), position.getY() - projectileShift);
		PrimedTnt primedTnt = new PrimedTnt(field, projectileInitial, projectileDestination);
		field.addProjectile(primedTnt);
	}
	
	@Override
	protected void graphicUpdate(GraphicsContext gc) {
		
		double drawX = position.getX() - width / 2;
		double drawY = position.getY();
		
		if (isSearching) {
			gc.drawImage(idle[direction], drawX, drawY);
		}
		
		if (isPrefiring || isPostfiring) {
			if (lifeCycle >= 0 && lifeCycle < 60) {
				gc.drawImage(firing[direction], drawX, drawY);
			}
			else {
				gc.drawImage(idle[direction], drawX, drawY);
			}
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
